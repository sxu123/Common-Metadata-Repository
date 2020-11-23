(/ns cmr.system-int-test.search.variable.variable-revisions-search-test
  "Integration test for variable all revisions search"
  (:require
   [clojure.test :refer :all]
   [cmr.common.util :refer [are3]]
   [cmr.mock-echo.client.echo-util :as e]
   [cmr.system-int-test.data2.core :as d]
   [cmr.system-int-test.data2.umm-json :as du]
   [cmr.system-int-test.data2.umm-spec-collection :as data-umm-c]
   [cmr.system-int-test.system :as s]
   [cmr.system-int-test.utils.association-util :as au]
   [cmr.system-int-test.utils.index-util :as index]
   [cmr.system-int-test.utils.ingest-util :as ingest]
   [cmr.system-int-test.utils.metadata-db-util :as metadata-db]
   [cmr.system-int-test.utils.search-util :as search]
   [cmr.system-int-test.utils.variable-util :as variable]
   [cmr.umm-spec.versioning :as umm-version]))

(use-fixtures :each
              (join-fixtures
               [(ingest/reset-fixture {"provguid1" "PROV1" "provguid2" "PROV2"})
                variable/grant-all-variable-fixture]))

(deftest search-variable-all-revisions
  (let [token (e/login (s/context) "user1")
        coll1 (d/ingest-umm-spec-collection "PROV1"
                                            (data-umm-c/collection 1 {})
                                            {:token token})
        coll2 (d/ingest-umm-spec-collection "PROV2"
                                            (data-umm-c/collection 1 {})
                                            {:token token})
        _ (index/wait-until-indexed)
        var1-concept (variable/make-variable-concept
                      {:Name "Variable1"
                       :provider-id "PROV1"}
                      {:native-id "var1"
                       :coll-concept-id (:concept-id coll1)})
        var1-1 (variable/ingest-variable-with-association var1-concept)
        var1-2-tombstone (merge (ingest/delete-concept var1-concept {:token token})
                                var1-concept
                                {:deleted true
                                 :user-id "user1"})

        var1-3 (variable/ingest-variable-with-association var1-concept)

        var2-1-concept (variable/make-variable-concept
                        {:Name "Variable2"
                         :LongName "LongName2"
                         :provider-id "PROV1"}
                        {:native-id "var2"
                         :coll-concept-id (:concept-id coll1)})
        var2-1 (variable/ingest-variable-with-association var2-1-concept)
        var2-2-concept (variable/make-variable-concept
                        {:Name "Variable2"
                         :LongName "LongName2-2"
                         :provider-id "PROV1"}
                        {:native-id "var2"
                         :coll-concept-id (:concept-id coll1)})
        var2-2 (variable/ingest-variable-with-association var2-2-concept)
        var2-3-tombstone (merge (ingest/delete-concept var2-2-concept {:token token})
                                var2-2-concept
                                {:deleted true
                                 :user-id "user1"})
        var3-concept (variable/make-variable-concept
                      {:Name "Variable1"
                       :LongName "LongName3"
                       :provider-id "PROV2"}
                      {:native-id "var3"
                       :coll-concept-id (:concept-id coll2)})
        var3 (variable/ingest-variable-with-association var3-concept)]
    (index/wait-until-indexed)
    (testing "search variables for all revisions"
      (are3 [variables params]
        (do
          ;; find references with all revisions
          (variable/assert-variable-references-match variables (search/find-refs :variable params))
          ;; search in JSON with all-revisions
          (variable/assert-variable-search variables (variable/search params))
          ;; search in UMM JSON with all-revisions
          (du/assert-variable-umm-jsons-match
           umm-version/current-variable-version variables
           (search/find-concepts-umm-json :variable params)))

        "provider-id all-revisions=false"
        [var1-3]
        {:provider-id "PROV1" :all-revisions false}

        "provider-id all-revisions unspecified"
        [var1-3]
        {:provider-id "PROV1"}

        "provider-id all-revisions=true"
        [var1-1 var1-2-tombstone var1-3 var2-1 var2-2 var2-3-tombstone]
        {:provider-id "PROV1" :all-revisions true}

        "native-id all-revisions=false"
        [var1-3]
        {:native-id "var1" :all-revisions false}

        "native-id all-revisions unspecified"
        [var1-3]
        {:native-id "var1"}

        "native-id all-revisions=true"
        [var1-1 var1-2-tombstone var1-3]
        {:native-id "var1" :all-revisions true}

        "name all-revisions false"
        [var1-3 var3]
        {:name "Variable1" :all-revisions false}

        ;; this test is across providers
        "name all-revisions unspecified"
        [var1-3 var3]
        {:name "Variable1"}

        "name all-revisions false"
        [var1-3 var3]
        {:name "Variable1" :all-revisions false}

        "name all-revisions true"
        [var1-1 var1-2-tombstone var1-3 var3]
        {:name "Variable1" :all-revisions true}

        "name all-revisions unspecified, latest revision is tombstone"
        []
        {:name "Variable2"}

        "name with all-revisions true, latest revision is tombstone"
        [var2-1 var2-2 var2-3-tombstone]
        {:name "Variable2" :all-revisions true}

        "all-revisions true"
        [var1-1 var1-2-tombstone var1-3 var2-1 var2-2 var2-3-tombstone var3]
        {:all-revisions true}))))

(deftest search-all-revisions-error-cases
  (testing "variable search with all_revisions bad value"
    (let [{:keys [status errors]} (search/find-refs :variable {:all-revisions "foo"})]
      (is (= [400 ["Parameter all_revisions must take value of true, false, or unset, but was [foo]"]]
             [status errors])))))
