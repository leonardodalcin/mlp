(ns mlp.entry)

; Define a interface para a classe LineGraph
(defprotocol EntryInterface
    (day [this])
    (open [this])
    (high [this])
    (low [this])
    (close [this])
    (volume [this])
    (market-cap [this])
)

; Define a classe LineGraph e seus atributos
(defrecord Entry [day open high low close volume market-cap]
    EntryInterface
    (day [this] (:day this))
    (open [this] (:open this))
    (high [this] (:high this))
    (low [this] (:low this))
    (close [this] (:close this))
    (volume [this] (:volume this))
    (market-cap [this] (:market-cap this))
)

; Não é possível criar construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors

; entry: Construtor para Entry
(defn entry [day open high low close volume market-cap]
    (Entry. day open high low close volume market-cap)
)
