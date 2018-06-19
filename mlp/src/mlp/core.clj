(ns mlp.core
  (:require [clj-http.client :as client]
            [jutsu.core :as j]
            [clojure.string :as str]
            [clojure.set :as set]
            [cheshire.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clj-postgresql.core :as pg]
            [java-jdbc.ddl :as ddl]
            [clj-time.core :as t]
            [clojure.math.numeric-tower :as math]))
(j/start-jutsu!)

(load "graph")
(load "market-data")

(defn -main
    "I can say 'Hello World'."
    []
    (print (createMarketData)))
