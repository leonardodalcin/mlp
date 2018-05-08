(defproject mlp "0.1.0-SNAPSHOT"
  :description "Web data scrapper"
  :url "https://github.com/leonardodalcin/mlp"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.9.0"]
                 [hswick/jutsu "0.1.2"]]
  :main ^:skip-aot mlp.core
  :target-path "target/%s"
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :profiles {:uberjar {:aot :all}})