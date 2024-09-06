package me.omega.currency_arbitrage

import kotlin.math.log10

data class RouteNode(val from: Currency, val to: Currency, val vertex: Int, val rate: Double) {
    val negativeLog = -log10(rate)
}