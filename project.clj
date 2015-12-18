(defproject hud "0.1.0"
  :dependencies [[org.clojure/clojure       "1.7.0"   :provided true]
                 [org.clojure/clojurescript "1.7.170" :provided true]
                 [reagent                   "0.5.1"   :provided true]]

  :source-paths ["src"]

  :plugins [[lein-cljsbuild "1.1.1"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :jar true
                        :compiler {:output-to "resources/public/js/compiled/hud.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src" "test"]
                        :notify-command ["phantomjs" "test/unit-test.js" "test/unit-test.html"]
                        :compiler {:optimizations :whitespace
                                   :pretty-print true
                                   :output-to "test/js/hud_test.js"
                                   :warnings {:single-segment-namespace false}}}]})
