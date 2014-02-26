(defproject golden_kestrel "0.1.0"
  :description "An Interactive Altmetric Embed Configurator of Supreme Excellence"
  :url "https://github.com/altmetric/golden-kestrel"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [om "0.3.6"]
                 [com.facebook/react "0.8.0.1"]]
  :plugins [[lein-cljsbuild "1.0.1"]
            [org.clojars.wokier/lein-bower "0.5.0"]]
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
                                   :pretty-print true
                                   :preamble ["js/components/es5-shim/es5-shim.min.js"
                                              "js/components/es5-shim/es5-sham.min.js"
                                              "react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]})
