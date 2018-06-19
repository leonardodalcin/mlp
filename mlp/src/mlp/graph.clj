; In REPL, run, for example:
;
; (def g (Graph. "test graph"))
; (plot g)
; (add-point g 1 1)
; (add-point g 2 3)
; (add-point g 3 2)
;
; This will plot a graph on the browser's window that Leiningen opened automatically
; and then update it with the added points.

(defprotocol GraphInterface
    (plot [this])
    (add-point [this x y]))

(defrecord Graph [name]
    GraphInterface
    (plot [this]
        (j/graph!
            (:name this)
            [{:x []
            :y []
            :type "scatter"}]))
    (add-point [this xValue yValue]
        (j/update-graph!
            (:name this)
            {:data {:y [[yValue]] :x [[xValue]]}
            :traces [0]}))
)
