(ns cmr.search.results-handlers.timeline-results-handler
  "Handles granule timeline interval results"
  (:require [cmr.search.data.elastic-results-to-query-results :as elastic-results]
            [cmr.search.data.elastic-search-index :as elastic-search-index]
            [cmr.search.services.query-execution :as query-execution]
            [cmr.search.services.query-service :as qs]
            [cmr.common.services.errors :as errors]
            [cmr.search.models.query :as q]
            [cheshire.core :as json]
            [cmr.search.models.results :as r]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(defn- interval->query-aggregations
  "TODO"
  [interval]
  {:by-collection
   {:terms {:field :collection-concept-id
            :size 10000}
    :aggregations {:start-date-intervals {:date_histogram {:field :start-date
                                                           :interval interval}}
                   :end-date-intervals {:date_histogram {:field :end-date
                                                         :interval interval}}}}})

(defmethod query-execution/pre-process-query-result-feature :timeline
  [context query feature]
  (let [{:keys [interval]} query
        temporal-cond (q/map->TemporalCondition (select-keys query [:start-date :end-date]))]
    (-> query
        (assoc :aggregations (interval->query-aggregations interval)
               :page-size 0
               :sort-keys nil)
        (update-in [:condition] #(q/and-conds [% temporal-cond])))))


(defmethod elastic-search-index/concept-type+result-format->fields [:granule :timeline]
  [concept-type query]
  ;; Timeline results are aggregation results so we select no fields
  [])


(defn event-comparator
  "Sorts events by event time. If event times match a start event comes before an end event."
  [e1 e2]
  (let [{type1 :event-type time1 :event-time} e1
        {type2 :event-type time2 :event-time} e2
        result (compare time1 time2)]
    (cond
      (not= 0 result) result
      (= type1 type2) 0
      (= type1 :start) -1
      (= type2 :start) 1
      :else (throw (Exception. "Logic error")))))


(defn- interval-bucket->event
  "TODO"
  [event-type bucket]
  {:event-type event-type
   :hits (:doc_count bucket)
   :event-time (c/from-long (:key bucket))})

(defn collection-bucket->ordered-events
  "TODO"
  [collection-bucket]
  (sort-by identity event-comparator
           (concat
             (map (partial interval-bucket->event :start)
                  (get-in collection-bucket [:start-date-intervals :buckets]))
             (map (partial interval-bucket->event :end)
                  (get-in collection-bucket [:end-date-intervals :buckets])))))

(def initial-interval
  "An empty intervals"
  {:start nil
   :end nil
   :curr-count 0
   :num-grans 0})

(defn ordered-events->intervals
  "TODO"
  [events]
  (loop [current-interval initial-interval
         events-left events
         intervals []]
    (if (empty? events-left)
      (if (not= current-interval initial-interval)
        ;; There was one or more granules that border the edge of the interval or didn't have end dates
        (conj intervals (assoc current-interval :no-end true))
        intervals)
      (let [{:keys [event-type hits event-time]} (first events-left)
            ;; Process the next event
            current-interval (if (= event-type :start)
                               ;; A start event
                               (-> current-interval
                                   (update-in [:curr-count] #(+ % hits))
                                   (update-in [:num-grans] #(+ % hits))
                                   (update-in [:start] #(or % event-time)))

                               ;; An end event
                               (-> current-interval
                                   (update-in [:curr-count] #(- % hits))
                                   (assoc :end event-time)))
            ;; Check to see if the current interval has ended
            [current-interval intervals] (if (= (:curr-count current-interval) 0)
                                           ;; Has ended
                                           [initial-interval (conj intervals current-interval)]
                                           ;; Not ended
                                           [current-interval intervals])]
        (recur current-interval (rest events-left) intervals)))))

(def interval-granularity->dist-fn
  "TODO"
  {:year t/in-years
   :month t/in-months
   :day t/in-days
   :hour t/in-hours
   :minute t/in-minutes
   :second t/in-seconds})

(defn adjacent?
  "Returns true if 2 intervals are adjacent based on the interval granularity. The order here is
  important. It assumes i2 follows i1."
  [interval-granularity i1 i2]
  (let [dist-fn (interval-granularity->dist-fn interval-granularity)]
    (= 1 (dist-fn (t/interval (:end i1) (:start i2))))))

(defn merge-intervals
  "TODO
  order is important"
  [i1 i2]
  (-> i1
      (update-in [:num-grans] #(+ % (:num-grans i2)))
      (assoc :end (:end i2))))

(defn merge-adjacent-intervals
  "TODO
  assumes intervals are already sorted by start date"
  [interval-granularity intervals]
  (loop [intervals intervals new-intervals [] prev nil]
    (if (empty? intervals)
      (conj new-intervals prev)
      (let [current (first intervals)
            [new-intervals prev] (cond
                                   ;; first interval
                                   (nil? prev)
                                   [new-intervals current]

                                   (adjacent? interval-granularity prev current)
                                   ;; Previous and current are adjacent. Merge them together
                                   [new-intervals (merge-intervals prev current)]

                                   :else
                                   ;; Normal case. Add prev to new-intervals.
                                   ;; current becomes the new prev
                                   [(conj new-intervals prev) current])]
        (recur (rest intervals) new-intervals prev)))))

(def interval-granularity->period-fn
  "Maps interval granularity types to the clj-time period functions"
  {:year t/years
   :month t/months
   :day t/days
   :hour t/hours
   :minute t/minutes
   :second t/seconds})


(defn advance-interval-end-date
  "Advances the interval end date by one unit of interval granularity. The end dates coming back from
  elasticsearch are at the beginning of the interval. The granules end date falls somewhere between
  the start of the interval and the end of it. By advancing the end date to the end we indicate to
  clients that there is data within that time area."
  [interval-granularity interval]
  (let [one-unit ((interval-granularity->period-fn interval-granularity) 1)]
    (update-in interval [:end] #(when % (t/plus % one-unit)))))

(defn constrain-interval-to-user-range
  "Constrains the start and end date of the interval to within the range given by the user"
  [start-date end-date interval]
  ;; This flag indicates in the ordered-events->intervals function that the interval had extra
  ;; granules that flowed over past the end of the last interval. This means we need to extend the
  ;; interval end date to the end of the range the user requested.
  (let [no-end (:no-end interval)]
    (-> interval
        (update-in [:start] #(if (t/before? % start-date) start-date %))
        (update-in [:end] #(if (or no-end (nil? %) (t/after? % end-date)) end-date %)))))

(defn collection-bucket->intervals
  "TODO"
  [interval-granularity start-date end-date collection-bucket]
  (let [collection-concept-id (:key collection-bucket)
        num-granules (:doc_count collection-bucket)
        intervals (->> collection-bucket
                       collection-bucket->ordered-events
                       ordered-events->intervals)
        interval-sum (reduce + (map :num-grans intervals))]
    (when (not= num-granules interval-sum)
      (errors/internal-error!
        (format "The sum of intervals, %s, did not match the count in the collection bucket, %s"
                interval-sum num-granules)))
    {:concept-id collection-concept-id
     :intervals (->> intervals
                     (merge-adjacent-intervals interval-granularity)
                     (map (partial advance-interval-end-date interval-granularity))
                     (map (partial constrain-interval-to-user-range start-date end-date)))}))


(def last-elastic-results (atom nil))

(defmethod elastic-results/elastic-results->query-results :timeline
  [context query elastic-results]
  (reset! last-elastic-results elastic-results)
  (let [{:keys [start-date end-date interval]} query
        items (map (partial collection-bucket->intervals (:interval query) start-date end-date)
                   (get-in elastic-results [:aggregations :by-collection :buckets]))]
    (r/map->Results {:items items
                     :result-format (:result-format query)})))

(comment

  (defn prettify-results
    [{:keys [items]}]
    (for [{:keys [concept-id intervals]} items]
      {:concept-id concept-id
       :intervals
       (for [{:keys [start end num-grans]} intervals]
         {:start (str start)
          :end (str end)
          :num-grans num-grans})}))

  (prettify-results
    (elastic-results/elastic-results->query-results
      nil {:result-format :timeline
           :interval :year} @last-elastic-results))

  )

(defn interval->response-tuple
  "TODO"
  [query {:keys [start end num-grans]}]
  [(/ (c/to-long start) 1000)
   (-> end
       ;; End may not be set if the granule didn't have an end date
       (or (:end-date query))
       c/to-long
       (/ 1000))
   num-grans])

(defn collection-result->response-result
  "TODO"
  [query coll-result]
  (update-in coll-result [:intervals] (partial map (partial interval->response-tuple query))))

(defmethod qs/search-results->response :timeline
  [context query results]
  (let [{:keys [items]} results
        response (map (partial collection-result->response-result query) items)]
    (json/generate-string response {:pretty (:pretty? query)})))





