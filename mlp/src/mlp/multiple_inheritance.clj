(defprotocol a
    (print [this]))

(defprotocol b
    (asd [this]))

(defprotocol c
    (qwe [this]))

(defrecord B []
    a
    b
    (print [this] "B")
    (asd [this] "qwe")
)

(defrecord C []
    a
    c
    (print [this] "C")
    (qwe [this] "qwe")
)
