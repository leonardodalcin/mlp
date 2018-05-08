(ns mlp.core
  (:require [clj-http.client :as client]
            [jutsu.core :as j]
            [clojure.string :as str]))

(j/start-jutsu!)
(Thread/sleep 1000)

(defn plot-points [graph-name data]
  (j/graph!
  graph-name
  [{:x (first data)
    :y (second data)
    :mode "markers"
    :type "scatter"}])
)

(defn parse-response [response]
  (vector (map read-string (str/split (first (str/split response #"/")) #" "))
          (map read-string (str/split (second (str/split response #"/")) #" ")))
)

(defn average
  [numbers]
    ( / (apply + numbers) (count numbers)))

(defn get-cpu-data [] (parse-response (:body (client/get "http://localhost:3000/mlp" {:headers {:access-token "1234"}}))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (plot-points "1" (get-cpu-data))
  ; (plot-points "2" (get-cpu-data))
)

;Construir um programa de mineração ou processamento de dados, onde os dados são colocados na entrada
; de maneira continua e o sistema deve realizar operações estatísticas sobre eles.
; Desvio padrao
; maximo
; minimo
; media
; plotar significancia https://en.wikipedia.org/wiki/Statistics#Mathematical_statistics
;
