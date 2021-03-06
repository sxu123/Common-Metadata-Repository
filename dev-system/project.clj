(def projects
  "A map of the other development projects to their versions"
  {:cmr-ingest-app "0.1.0-SNAPSHOT"
   :cmr-search-app "0.1.0-SNAPSHOT"
   :cmr-indexer-app "0.1.0-SNAPSHOT"
   :cmr-bootstrap-app "0.1.0-SNAPSHOT"
   :cmr-cubby-app "0.1.0-SNAPSHOT"
   :cmr-access-control-app "0.1.0-SNAPSHOT"
   :cmr-virtual-product-app "0.1.0-SNAPSHOT"
   :cmr-metadata-db-app "0.1.0-SNAPSHOT"
   :cmr-index-set-app "0.1.0-SNAPSHOT"
   :cmr-common-lib "0.1.1-SNAPSHOT"
   :cmr-acl-lib "0.1.0-SNAPSHOT"
   :cmr-transmit-lib "0.1.0-SNAPSHOT"
   :cmr-collection-renderer-lib "0.1.0-SNAPSHOT"
   :cmr-spatial-lib "0.1.0-SNAPSHOT"
   :cmr-es-spatial-plugin "0.1.0-SNAPSHOT"
   :cmr-umm-lib "0.1.0-SNAPSHOT"
   :cmr-umm-spec-lib "0.1.0-SNAPSHOT"
   :cmr-elastic-utils-lib "0.1.0-SNAPSHOT"
   :cmr-system-int-test "0.1.0-SNAPSHOT"
   :cmr-oracle-lib "0.1.0-SNAPSHOT"
   :cmr-orbits-lib "0.1.0-SNAPSHOT"
   :cmr-mock-echo-app "0.1.0-SNAPSHOT"
   :cmr-message-queue-lib "0.1.0-SNAPSHOT"
   :cmr-common-app-lib "0.1.0-SNAPSHOT"})

(def project-dependencies
  "A list of other projects as maven dependencies"
  (doall (map (fn [[project-name version]]
                (let [maven-name (symbol "nasa-cmr" (name project-name))]
                  [maven-name version]))
              projects)))

(def create-checkouts-commands
  (vec
    (apply concat ["do"
                   "shell" "mkdir" "checkouts,"]
           (map (fn [project-name]
                  ["shell" "ln" "-s" (str "../../" (subs (name project-name) 4)) "checkouts/,"])
                (keys projects)))))

;; The version number here is for the sprint number. It will be incremented each sprint. The second
;; number is for which delivery of the version was given to ECHO for use.
(defproject nasa-cmr/cmr-dev-system "0.1.0-SNAPSHOT"
  :description "Dev System combines together the separate microservices of the CMR into a single
               application to make it simpler to develop."
  :url "***REMOVED***browse/dev-system"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies ~(concat '[[org.clojure/clojure "1.8.0"]
                           [org.clojure/tools.nrepl "0.2.12"]
                           ;; Add groovy to support groovy scripting in elastic
                           [org.codehaus.groovy/groovy-all "2.4.0"]]
                         project-dependencies)
  :plugins [[lein-shell "0.4.0"]
            [test2junit "1.2.1"]
            [lein-environ "1.1.0"]]
  :repl-options {:init-ns user
                 :timeout 180000}
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow"
             "-Dclojure.compiler.direct-linking=true"
             ;; Enable logging in jetty.
             "-Dorg.eclipse.jetty.util.log.class=org.eclipse.jetty.util.log.StrErrLog"
             "-Dorg.eclipse.jetty.LEVEL=INFO"
             "-Dorg.eclipse.jetty.websocket.LEVEL=INFO"]
  :profiles {
    :dev-dependencies {:dependencies [[ring-mock "0.1.5"]
                                      [org.clojure/tools.namespace "0.2.11"]
                                      [org.clojars.gjahad/debug-repl "0.3.3"]
                                      [pjstadig/humane-test-output "0.8.1"]
                                      [debugger "0.2.0"]
                                      [criterium "0.4.4"]
                                      ;; Must be listed here as metadata db depends on it.
                                      [drift "1.5.3"]
                                      [proto-repl-charts "0.3.1"]
                                      [proto-repl "0.3.1"]
                                      [proto-repl-sayid "0.1.3"]]
                       ;; XXX Note that profiling can be kept in a profile,
                       ;;     with no need to comment/uncomment.
                       ;; Use the following to enable JMX profiling with visualvm
                       ;:jvm-opts ^:replace ["-server"
                       ;                     "-Dcom.sun.management.jmxremote"
                       ;                     "-Dcom.sun.management.jmxremote.ssl=false"
                       ;                     "-Dcom.sun.management.jmxremote.authenticate=false"
                       ;                     "-Dcom.sun.management.jmxremote.port=1098"]
                       :source-paths ["src" "dev" "test"]
                       :injections [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
    ;; This is to separate the dependencies from the dev-config specified in profiles.clj
    :dev [:dev-dependencies :dev-config]
    :uberjar {:main cmr.dev-system.runner
              ;; See http://stephen.genoprime.com/2013/11/14/uberjar-with-titan-dependency.html
              :uberjar-merge-with {#"org\.apache\.lucene\.codecs\.*" [slurp str spit]}
              :aot :all}
    ;; This profile is used for linting and static analysis. To run for this
    ;; project, use `lein lint` from inside the project directory. To run for
    ;; all projects at the same time, use the same command but from the top-
    ;; level directory.
    :lint {
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [[jonase/eastwood "0.2.3"]
                [lein-ancient "0.6.10"]
                [lein-bikeshed "0.4.1"]
                [lein-kibit "0.1.2"]
                [lein-shell "0.4.0"]
                [venantius/yagni "0.1.4"]]}}
  :aliases {;; Creates the checkouts directory to the local projects
            "create-checkouts" ~create-checkouts-commands
            ;; Alias to test2junit for consistency with lein-test-out
            "test-out" ["test2junit"]
            ;; Installs the Elasticsearch Marvel plugin locally.
            ;; Visit http://localhost:9210/_plugin/marvel/sense/index.html
            "install-marvel" ["shell" "./support/install-marvel.sh"]
            ;; Linting aliases
            "kibit" ["do" ["with-profile" "lint" "shell" "echo" "== Kibit =="]
                          ["with-profile" "lint" "kibit"]]
            "eastwood" ["with-profile" "lint" "eastwood" "{:namespaces [:source-paths]}"]
            "bikeshed" ["with-profile" "lint" "bikeshed"]
            "yagni" ["with-profile" "lint" "yagni"]
            "check-deps" ["with-profile" "lint" "ancient"]
            "lint" ["do" ["check"] ["kibit"] ["eastwood"]]})
