(ns mlp.line-graph
    (:require
        [jutsu.core :as j]
        [mlp.graph :as graph]
        [mlp.entry :as entry]))

; Define a interface para a classe LineGraph
(defprotocol LineGraphInterface
    (get-close [this]))

; Define a classe LineGraph e seus atributos
(defrecord LineGraph [name entries]
    ; Implementa GraphInterface
    graph/GraphInterface
    ; Implementa LineGraphInterface
    LineGraphInterface
    (plot [this]
        (j/graph!
            (:name this)
            [{
                :x (graph/get-day this) ; Ao chamar uma função definida numa interface em outro namespace, usa-se o namespace onde ela foi definida
                :y (get-close this)
                :type "scatter"
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

; line-graph: Construtor para LineGraph
(defn line-graph [name entries]
    (LineGraph. name entries)
)
