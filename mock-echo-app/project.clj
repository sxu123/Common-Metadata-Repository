(defproject nasa-cmr/cmr-mock-echo-app "0.1.0-SNAPSHOT"
  :description "Mocks out the ECHO REST API."
  :url "***REMOVED***browse/mock-echo-app"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [nasa-cmr/cmr-transmit-lib "0.1.0-SNAPSHOT"]
                 [nasa-cmr/cmr-common-app-lib "0.1.0-SNAPSHOT"]
                 [compojure "1.5.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[test2junit "1.2.1"]]
  :repl-options {:init-ns user}
  :jvm-opts ^:replace ["-server"
                       "-Dclojure.compiler.direct-linking=true"]
  :profiles {
    :dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                         [org.clojars.gjahad/debug-repl "0.3.3"]
                         [nasa-cmr/cmr-transmit-lib "0.1.0-SNAPSHOT"]]
          :jvm-opts ^:replace ["-server"]
          :source-paths ["src" "dev" "test"]}
    :uberjar {:main cmr.mock-echo.runner
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
  :aliases {;; Alias to test2junit for consistency with lein-test-out
            "test-out" ["test2junit"]
            ;; Linting aliases
            "kibit" ["do" ["with-profile" "lint" "shell" "echo" "== Kibit =="]
                          ["with-profile" "lint" "kibit"]]
            "eastwood" ["with-profile" "lint" "eastwood" "{:namespaces [:source-paths]}"]
            "bikeshed" ["with-profile" "lint" "bikeshed"]
            "yagni" ["with-profile" "lint" "yagni"]
            "check-deps" ["with-profile" "lint" "ancient"]
            "lint" ["do" ["check"] ["kibit"] ["eastwood"]]})
