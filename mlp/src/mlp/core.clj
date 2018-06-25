(ns mlp.core
    (:require
        [jutsu.core :as j]
        [mlp.graph :as graph]
        [mlp.line-graph :as line]
        [mlp.candle-graph :as candle]
        [mlp.ohlc-graph :as ohlc]
        [mlp.bar-graph :as bar]
        [mlp.data-fetcher :as fetcher]
    )
)
(j/start-jutsu!)

(defn -main []
    (print "Chame a função (plot type interval) para mostrar os gráficos.")
)

(defn plot-data [type data name]
    (case type
        "line" (graph/plot (line/line-graph name data))
        "bar" (graph/plot (bar/bar-graph name data))
        "candle" (graph/plot (candle/candle-graph name data))
        "ohlc" (graph/plot (ohlc/ohlc-graph name data))
        (throw (Exception. "Graph type must be one of the following: 'line','bar','candle' or 'ohlc'"))
    )
)

; Polimorfismo paramétrico
(defn plot-by-interval
    ([type interval] (plot-data type (fetcher/fetch (fetcher/data-fetcher interval)) "Bitcoin"))
    ([type interval name] (plot-data type (fetcher/fetch (fetcher/data-fetcher interval)) name))
)
