
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
(defn -main[]
  ; (def market-data (get (get-market-data) (keyword "Time Series (Daily)")))
  ; (def market-data-days (keys market-data))
  ; (save-recursive market-data market-data-days)
  ; (def database-data (into [] (get-all)))
  ; (plot-points "Test" (map (fn [obj] (get obj :date)) database-data) (map (fn [obj] (get obj :high)) database-data))
  (print (EMA (10 15) 0.5))
)
