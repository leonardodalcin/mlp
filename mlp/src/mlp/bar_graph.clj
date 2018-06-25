(ns mlp.bar-graph
    (:require
        [jutsu.core :as j]
        [mlp.graph :as graph]
        [mlp.entry :as entry]))

; Define a interface para a classe LineGraph
(defprotocol BarGraphInterface
    (get-close [this]))

; Define a classe LineGraph e seus atributos
(defrecord BarGraph [name entries]
    ; Implementa GraphInterface
    graph/GraphInterface
    ; Implementa BarGraphInterface
    BarGraphInterface
    (plot [this]
        (j/graph!
            (:name this)
            [{
                :x (graph/get-day this) ; Ao chamar uma função definida numa interface em outro namespace, usa-se o namespace onde ela foi definida
                :y (get-close this)
                :type "bar"
            }]
        )
    )
    (get-day [this]
        (for [e (:entries this)]
            (entry/day e)
        )
    )
    (get-close [this]
        (for [e (:entries this)]
            (entry/close e)
        )
    )
)

; Não é possível criar construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors

; bar-graph: Construtor para BarGraph
(defn bar-graph [name entries]
    (BarGraph. name entries)
)
