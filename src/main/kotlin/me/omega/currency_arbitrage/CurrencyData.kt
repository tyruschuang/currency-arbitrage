package me.omega.currency_arbitrage

import kotlin.math.log10

class CurrencyData(
    val currencies: Array<Currency>,
    val exchangeRates: Array<DoubleArray>
) {

   val edges: Array<Edge> by lazy {
       var edges = arrayOf<Edge>()
        Array(currencies.size) { i ->
            Array(currencies.size) { j ->
                edges += Edge(currencies[i], currencies[j], exchangeRates[i][j])
            }
        }
       edges
   }

    val negativeLogRates: Array<DoubleArray> by lazy {
        Array(currencies.size) { i ->
            DoubleArray(currencies.size) { j ->
                -log10(exchangeRates[i][j])
            }
        }
    }

    val logRates: Array<DoubleArray> by lazy {
        Array(currencies.size) { i ->
            DoubleArray(currencies.size) { j ->
                log10(exchangeRates[i][j])
            }
        }
    }

   fun getEdge(from: Currency, to: Currency): Edge {
        return getEdge(currencies.indexOf(from), currencies.indexOf(to))
   }

    fun getEdge(from: Int, to: Int): Edge {
        return edges.first { it.from == currencies[from] && it.to == currencies[to] }
    }
}