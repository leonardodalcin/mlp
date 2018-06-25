# Trabalho Final de MLP 2018/1 - UFRGS

## Grupo

* Gabriel Tiburski Júnior
* Leonardo Dalcin
* Giovani

## Sobre o Trabalho

Devemos utilizar a linguagem Clojure para fazer processamento de dados. Decidímos trabalhar com os valores das criptomoedas com o passar do tempo. Para isso, buscamos na API do AlphaVantage o histórico de valores do BitCoin e permitimos a impressão de alguns tipos diferentes de gráficos na tela para visualizar essa informação.

## Instruções para execução

Antes de tudo, instale o Clojure e o Leiningen. Depois disso, vá para a pasta `mlp/src/mlp` e execute:

``lein repl``

Este comando abrirá o REPL do Leiningen, onde poderemos executar as funções que desenvolvemos.

## Solução Orientada a Objetos

Para mostrar algum gráfico na tela, chame a função:

``(plot-by-interval type interval name)``

Onde:

* `type` deve ser `"line"`, `"bar"`, `"candle"` ou `"ohlc"`;
* `interval` deve ser `"daily"`, `"weekly"` ou `"monthly"`;
* `name` é opcional e pode ser qualquer string;


## Solução Funcional

Ainda precisamos fazer
