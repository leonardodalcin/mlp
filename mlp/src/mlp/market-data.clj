; Interface para a criação da classe logo abaixo
(defprotocol MarketDataInterface)

; MarketData possui um construtor padrão que armazena o conteúdo do parâmetro 'db'
; para acessá-lo, é necessário usar (:db this)
(defrecord MarketData [data]
    ; Herda da interface MarketDataInterface
    MarketDataInterface)

; Não é possível fazer construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors
(defn createMarketData []
    ; Busca pelo histórico de valores de BitCoin na API do site AlphaVantage e
    ; constrói uma classe do tipo MarketData
    (sort-by :day
        (let [raw-data (parse-string (:body (client/get "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=MSFT&apikey=demo")), true)]
            (let [data (get raw-data (keyword "Time Series (Daily)"))]
                (for [d data]
                    {
                        :day (name (key d))
                        :value (get-in d [1 (keyword "4. close")])
                    })
            )
        )
    )
)
