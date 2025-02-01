package me.omega.currency_arbitrage

import kotlin.math.log10

data class Edge(val from: Currency, val to: Currency, val rate: Double) {
    val negativeLog = -log10(rate)
}