(ns mlp.data-fetcher
    (:require
        [clj-http.client :as client]
        [cheshire.core :refer :all]
        [mlp.entry :as e]))

; Interface para a classe dataFetcher
(defprotocol DataFetcherInterface
    (fetch [this]))

; DataFetcher é a classe responsável por buscar os dados na API do AlphaVantage
; com o intervalo definido em seu construtor (diário, semanal ou mensal)
; Para criar uma nova instâncias, utilize o construtor data-fetcher definido
; abaixo (data-fetcher [interval])
; Para acessar seus atributos, utilize (:nome_do_atributo this)
(defrecord DataFetcher [url containerLabel]
    ; Herda da interface dataFetcherInterface
    DataFetcherInterface
    ; Busca os dados de acordo com seus parâmetros
    (fetch [this]
        (sort-by :day
            (let [raw-data (parse-string (:body (client/get (:url this))), true)]
                (let [data (get raw-data (keyword (:containerLabel this)))]
                    (for [d data]
                        (e/entry
                            (name (key d))
                            (get-in d [1 (keyword "1b. open (USD)")])
                            (get-in d [1 (keyword "2b. high (USD)")])
                            (get-in d [1 (keyword "3b. low (USD)")])
                            (get-in d [1 (keyword "4b. close (USD)")])
                            (get-in d [1 (keyword "5. volume")])
                            (get-in d [1 (keyword "6. market cap (USD)")])
                        )
                    )
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
(defn data-fetcher [interval]
    (case interval
        "daily" (DataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Daily)")
        "weekly" (DataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_WEEKLY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Weekly)")
        "monthly" (DataFetcher. "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_MONTHLY&symbol=BTC&market=CNY&apikey=demo" "Time Series (Digital Currency Monthly)")
        (throw (Exception. "The data interval can only be 'daily','weekly' or 'monthly'"))
    )
)
