(ns mlp.graph)

; Define a interface para a classe Graph que será herdada pelos outros gráficos
(defprotocol GraphInterface
    (plot [this])
    (get-day [this]))
