(defproject hanbaiki "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.omcljs/om "1.0.0-alpha22-snapshot"]
                 [org.clojure/data.json "0.2.6"]
                 [org.omcljs/ambly "0.6.0"]
                 [com.cognitect/transit-clj "0.8.281"]
                 [com.cognitect/transit-cljs "0.8.225"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-http "0.1.30" :exclusions
                  [org.clojure/clojure org.clojure/clojurescript
                   com.cognitect/transit-cljs]]
                 [com.cemerick/piggieback "0.2.0"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.nrepl "0.2.10"]]}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

:plugins [[lein-cljsbuild "1.1.1"]]
:cljsbuild {:builds {:dev {:source-paths ["src"]
                           :compiler {:output-to "target/out/main.js"
                                      :output-dir "target/out"
                                      :optimizations :none}}}})
