; Interface para a criação da classe logo abaixo
(defprotocol MarketDataInterface
    (get-from-web [this])
    (get-from-db [this])
    (save-recursive [this market-data market-data-days]))

; MarketData possui um construtor padrão que armazena o conteúdo do parâmetro 'db'
; para acessá-lo, é necessário usar (:db this)
(defrecord MarketData [db]
    ; Herda da interface MarketDataInterface
    MarketDataInterface
    ; Busca dados sobre o valor de BitCoin na API do site AlphaVantage
    (get-from-web [this]
        (parse-string (:body (client/get "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=MSFT&apikey=demo")), true))
    ; Busca dados já salvos no banco
    (get-from-db [this]
        (into [] (jdbc/query (:db this) ["select date, high from stocks"])))
    ; Salva os dados recuperados no banco de dados
    (save-recursive [this market-data market-data-days]
        (case (into [] market-data-days)
            [] 0
            (try
                ((jdbc/insert! (:db this) :stocks
                {
                    :date (name (first market-data-days))
                    :open (get-in market-data [(first market-data-days) (keyword "1. open")])
                    :high (get-in market-data [(first market-data-days) (keyword "2. high")])
                    :low (get-in market-data [(first market-data-days) (keyword "3. low")])
                    :close (get-in market-data [(first market-data-days) (keyword "4. close")])
                    :adjustedclose (get-in market-data [(first market-data-days) (keyword "5. adjusted close")])
                    :volume (get-in market-data [(first market-data-days) (keyword "6. volume")])
                    :dividendamount (get-in market-data [(first market-data-days) (keyword "7. dividend amount")])
                    :splitcoefficient (get-in market-data [(first market-data-days) (keyword "8. split coefficient")])
                }
                ) (save-recursive this market-data (drop 1 market-data-days)))
                (catch Exception e (save-recursive this market-data (drop 1 market-data-days)))))))

; Não é possível fazer construtores customizados em Clojure,
; então é necessário fazer uma função separada para atuar como construtor.
; https://stuartsierra.com/2015/05/17/clojure-record-constructors
(defn createMarketData []
    "Creates a new MarketData class."
    ; Gera uma conexão com o banco de dados
    (let [db (pg/pool :host "localhost" :port "5432" :user "postgres" :dbname "postgres" :password "postgres")]
        (let [marketdata (MarketData. db)]
            (do
                ; Cria uma tabela para armazenar os dados sobre BitCoins
                (try (jdbc/db-do-commands db
                    (ddl/create-table :stocks
                        [:date "character varying"]
                        [:date "character varying" :key :primary]
                        [:open "character varying"]
                        [:high "character varying"]
                        [:low "character varying"]
                        [:close "character varying"]
                        [:adjustedclose "character varying"]
                        [:volume "character varying"]
                        [:dividendamount "character varying"]
                        [:splitcoefficient "character varying"]))
                        (catch Exception e (print "Database already created\n")))
                marketdata))))
