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



(def db (pg/pool :host "localhost" :port "5432" :user "postgres" :dbname "postgres" :password "postgres"))
(try (jdbc/db-do-commands db
  (ddl/create-table :stocks
    [:date "character varying"]
    [:date "character varying" :key :primary]
    [:open "character varying"]
    [:high "character varying"]
    [:low "character varying"]
    [:close "character varying"]
    [:adjustedclose "character varying"]
    [:volume "character varying"]
    [:dividendamount "character varying"]
    [:splitcoefficient "character varying"]))
    (catch Exception e (print "Database already created\n")))

(defn plot-points [graph-name x y]
  (print x)
  (j/graph!
  graph-name
  [{:x x
    :y y
    :mode "markers"
    :type "scatter"}])
)

(defn get-all [] (into [] (jdbc/query db ["select date, high from stocks"])))

(defn save-recursive [market-data market-data-days] (
  case (into [] market-data-days)
    [] 0
    (try
      ((jdbc/insert! db :stocks
        {
         :date (name (first market-data-days))
         :open (get-in market-data [(first market-data-days) (keyword "1. open")])
         :high (get-in market-data [(first market-data-days) (keyword "2. high")])
         :low (get-in market-data [(first market-data-days) (keyword "3. low")])
         :close (get-in market-data [(first market-data-days) (keyword "4. close")])
         :adjustedclose (get-in market-data [(first market-data-days) (keyword "5. adjusted close")])
         :volume (get-in market-data [(first market-data-days) (keyword "6. volume")])
         :dividendamount (get-in market-data [(first market-data-days) (keyword "7. dividend amount")])
         :splitcoefficient (get-in market-data [(first market-data-days) (keyword "8. split coefficient")])
        }
      ) (save-recursive market-data (drop 1 market-data-days)))
      (catch Exception e (save-recursive market-data (drop 1 market-data-days))))
))

(defn list-add [values]
  (if (empty? values)
      0
      (+ (first values) (list-add (rest values)))
  )
)

(defn list-cut [l iterator]
    (if (or (empty? l) (= iterator 1))
        (list (first l))
        (conj (list-cut (rest l) (- iterator 1)) (first l))
    )
)

(defn list-reverse [l]
    (if (empty? (rest l))
        (list (first l))
        (concat (list-reverse (rest l)) (list (first l)))
    )
)

(defn simple-moving-average [values period]
    (/ (list-add (list-cut values period)) period)
)
; alpha * [p1 + (1-alpha)p2 + (1-alpha)^2p3 ...]
(defn EMA [values alpha] (map-indexed (fn [index value] (* value (math/expt (- 1 alpha) index))) values))
  ; (def result (reduce + mapped))
  ; (print result)
  ; result
(defn get-market-data [] (parse-string (:body (client/get "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=MSFT&apikey=demo")), true))
(defn -main[]
  ; (def market-data (get (get-market-data) (keyword "Time Series (Daily)")))
  ; (def market-data-days (keys market-data))
  ; (save-recursive market-data market-data-days)
  ; (def database-data (into [] (get-all)))
  ; (plot-points "Test" (map (fn [obj] (get obj :date)) database-data) (map (fn [obj] (get obj :high)) database-data))
  (print (EMA (10 15) 0.5))
)
