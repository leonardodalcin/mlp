(ns mlp.ohlc-graph
    (:require
        [jutsu.core :as j]
        [mlp.graph :as graph]
        [mlp.entry :as entry]))

; Define a interface para a classe LineGraph
(defprotocol OhlcGraphInterface
    (get-close [this])
    (get-high [this])
    (get-low [this])
    (get-open [this])
)

; Define a classe LineGraph e seus atributos
(defrecord OhlcGraph [name entries]
    ; Implementa GraphInterface
    graph/GraphInterface
    ; Implementa OhlcGraphInterface
    OhlcGraphInterface
    (plot [this]
        (j/graph!
            (:name this)
            [{
                :x (graph/get-day this) ; Ao chamar uma função definida numa interface em outro namespace, usa-se o namespace onde ela foi definida
                :close (get-close this)
                :high (get-high this)
                :low (get-low this)
                :open (get-open this)
                :type "ohlc"
            }]
        )
    )
    (get-day [this] (for [e (:entries this)] (entry/day e)))
    (get-close [this] (for [e (:entries this)] (entry/close e)))
    (get-high [this] (for [e (:entries this)] (entry/high e)))
    (get-low [this] (for [e (:entries this)] (entry/low e)))
    (get-open [this] (for [e (:entries this)] (entry/open e)))
)

; Não é possível criar construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors

; ohlc-graph: Construtor para OhlcGraph
(defn ohlc-graph [name entries]
    (OhlcGraph. name entries)
)
