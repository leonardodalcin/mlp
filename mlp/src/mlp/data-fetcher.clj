; Interface para a classe dataFetcher
(defprotocol dataFetcherInterface
    (fetch [this]))

; DataFetcher é a classe responsável por buscar os dados na API do AlphaVantage
; com o intervalo definido em seu construtor (diário, semanal ou mensal)
; Para criar uma nova instâncias, utilize o construtor DataFetcher definido
; abaixo (DataFetcher [interval])
; Para acessar seus atributos, utilize (:nome_do_atributo this)
(defrecord dataFetcher [url containerLabel valueLabel]
    ; Herda da interface dataFetcherInterface
    dataFetcherInterface
    ; Busca os dados de acordo com seus parâmetros
    (fetch [this]
        (sort-by :day
            (let [raw-data (parse-string (:body (client/get (:url this))), true)]
                (let [data (get raw-data (keyword (:containerLabel this)))]
                    (for [d data]
                        {
                            :day (name (key d))
                            :value (get-in d [1 (keyword (:valueLabel this))])
                        })
                )
            )
        )
    )
)

; Não é possível criar construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors

; DataFetcher é o construtor para a classe dataFetcher
; Parâmetros: interval, deve ser "daily", "weekly" ou "monthly"
; caso contrário, retorna uma exceção
(defn DataFetcher [interval]
    (case interval
        "daily" (dataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Daily)" "4b. close (USD)")
        "weekly" (dataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Weekly)" "4b. close (USD)")
        "monthly" (dataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_MONTHLY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Monthly)" "4b. close (USD)")
        (throw (Exception. "The data interval can only be 'daily','weekly' or 'monthly'"))
    )
)
