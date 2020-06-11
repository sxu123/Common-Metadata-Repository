(ns cmr.system-int-test.search.subscription.subscription-revisions-search-test
  "Integration test for search all revisions search"
  (:require
   [clojure.test :refer :all]
   [cmr.common.util :refer [are3]]
   [cmr.mock-echo.client.echo-util :as e]
   [cmr.system-int-test.data2.core :as d]
   [cmr.system-int-test.data2.umm-json :as du]
   [cmr.system-int-test.system :as s]
   [cmr.system-int-test.utils.index-util :as index]
   [cmr.system-int-test.utils.ingest-util :as ingest]
   [cmr.system-int-test.utils.search-util :as search]
   [cmr.system-int-test.utils.subscription-util :as subscription]
   [cmr.umm-spec.versioning :as umm-version]))

(use-fixtures :each
              (join-fixtures
               [(ingest/reset-fixture {"provguid1" "PROV1" "provguid2" "PROV2"})
                (subscription/grant-all-subscription-fixture 
                  {"provguid1" "PROV1" "provguid2" "PROV2"} [:read :update])]))

(deftest search-subscription-all-revisions
  (let [token (e/login (s/context) "user1")
        sub1-concept (subscription/make-subscription-concept {:native-id "SUB1"
                                                    :Name "Subscription1"
                                                    :provider-id "PROV1"})
        sub2-concept (subscription/make-subscription-concept {:native-id "SUB2"
                                                    :Name "Subscription2"
                                                    :provider-id "PROV1"})
        sub2-2-concept (subscription/make-subscription-concept {:native-id "SUB2"
                                                      :Name "Subscription2-2"
                                                      :provider-id "PROV1"})
        sub3-concept (subscription/make-subscription-concept {:native-id "SUB3"
                                                    :Name "Subscription1"
                                                    :provider-id "PROV2"})
        sub1-1 (subscription/ingest-subscription sub1-concept)
        sub1-2-tombstone (merge (ingest/delete-concept
                                 sub1-concept (subscription/token-opts token))
                                sub1-concept
                                {:deleted true
                                 :user-id "user1"})
        sub1-3 (subscription/ingest-subscription sub1-concept)
        sub2-1 (subscription/ingest-subscription sub2-concept)
        sub2-2 (subscription/ingest-subscription sub2-2-concept)
        sub2-3-tombstone (merge (ingest/delete-concept
                                 sub2-2-concept (subscription/token-opts token))
                                sub2-2-concept
                                {:deleted true
                                 :user-id "user1"})
        sub3 (subscription/ingest-subscription sub3-concept)]
    (index/wait-until-indexed)
    (testing "search subscriptions for all revisions"
      (are3 [subscriptions params]
        (do
          ;; find references with all revisions
          (d/assert-refs-match
            subscriptions (subscription/search-refs params))
          ;; search in JSON with all-revisions
          (subscription/assert-subscription-search subscriptions (subscription/search-json params))
          ;; search in UMM JSON with all-revisions
          (du/assert-subscription-umm-jsons-match
           umm-version/current-subscription-version
           subscriptions
           (search/find-concepts-umm-json :subscription params)))

        "provider-id all-revisions=false"
        [sub1-3]
        {:provider-id "PROV1" :all-revisions false}

        "provider-id all-revisions unspecified"
        [sub1-3]
        {:provider-id "PROV1"}

        "provider-id all-revisions=true"
        [sub1-1 sub1-2-tombstone sub1-3 sub2-1 sub2-2 sub2-3-tombstone]
        {:provider-id "PROV1" :all-revisions true}

        "native-id all-revisions=false"
        [sub1-3]
        {:native-id "sub1" :all-revisions false}

        "native-id all-revisions unspecified"
        [sub1-3]
        {:native-id "sub1"}

        "native-id all-revisions=true"
        [sub1-1 sub1-2-tombstone sub1-3]
        {:native-id "sub1" :all-revisions true}

        ;; this test is across providers
        "name all-revisions false"
        [sub1-3 sub3]
        {:name "Subscription1" :all-revisions false}

        ;; this test is across providers
        "name all-revisions unspecified"
        [sub1-3 sub3]
        {:name "Subscription1"}

        "name all-revisions true"
        [sub1-1 sub1-2-tombstone sub1-3 sub3]
        {:name "Subscription1" :all-revisions true}

        "name is updated on revision -- not found without all-revisions true"
        []
        {:name "Subscription2"}

        "name is updated on revision -- found with all-revisions true"
        [sub2-1]
        {:name "Subscription2" :all-revisions true}

        "all-revisions true"
        [sub1-1 sub1-2-tombstone sub1-3 sub2-1 sub2-2 sub2-3-tombstone sub3]
        {:all-revisions true}))))

(deftest search-all-revisions-error-cases
  (testing "subscription search with all_revisions bad value"
    (let [{:keys [status errors]} (search/find-refs :subscription {:all-revisions "foo"})]
      (is (= [400 ["Parameter all_revisions must take value of true, false, or unset, but was [foo]"]]
             [status errors])))))
