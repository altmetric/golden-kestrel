(defproject golden_kestrel "0.1.0-SNAPSHOT"
  :description "An Interactive Altmetric Embed Configurator of Supreme Excellence"
  :url "https://github.com/digital-science/golden-kestrel"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [om "0.2.3"]
                 [com.facebook/react "0.8.0.1"]]
  :plugins [[lein-cljsbuild "1.0.1"]]
  :source-paths ["src"]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {:output-to "out/kestrel-dev.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}
                       {:id "release"
                        :source-paths ["src"]
                        :compiler {:output-to "kestrel-release.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :source-map "source.js.map"
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]})
