(ns parcial.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
)

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

(defn ema [values original period]
    (def k (/ 2 (+ period 1)))
    (def current (first values))

    (if (empty? (rest values))
        (do
            (def sma (simple-moving-average original period))
            (+ (* (- current sma) k) sma)
        )
        (do
            (def last-ema (ema (rest values) original period))
            (+ (* (- current last-ema) k) last-ema)
        )
    )
)

(defn exponential-moving-average [values period]
    (def cutted (list-cut values period))
    (ema cutted cutted period)
)
