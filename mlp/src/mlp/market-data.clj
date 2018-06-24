; Interface para a criação da classe logo abaixo
(defprotocol marketDataInterface
    (show [this]))

; marketData possui um construtor padrão que armazena o conteúdo do parâmetro 'db'
; para acessá-lo, é necessário usar (:data this)
(defrecord marketData [data]
    ; Herda da interface MarketDataInterface
    marketDataInterface
    ; Imprime um gráfico com os dados resgatados
    (show [this]
        (let [g (Graph. "test graph")]
            (do
                (plot g)
                (map (fn [entry] (add-point g (get entry :day) (get entry :value))) (:data this))))))

; Não é possível fazer construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors

; Este construtor está aqui apenas como açucar sintático
; Ele simplesmente cria uma instância de marketData com os mesmos dados passados
(defn MarketData [data]
    (marketData. data)
)
