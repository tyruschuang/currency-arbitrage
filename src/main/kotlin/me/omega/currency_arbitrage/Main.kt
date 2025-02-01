package me.omega.currency_arbitrage

fun main() {
    val currencies = Currency.entries.toTypedArray()
    val exchangeRates = arrayOf(
        /*                       CNY      EUR      JPY      NZD     USD  */
        /* CNY */ doubleArrayOf(1.0000, 0.1406, 19.86490, 0.2318, 0.1503),
        /* EUR */ doubleArrayOf(7.2861, 1.0000, 138.8341, 1.6753, 1.0797),
        /* JPY */ doubleArrayOf(0.0527, 0.0071, 1.000000, 0.0120, 0.0076),
        /* NZD */ doubleArrayOf(4.3228, 0.5962, 78.26350, 1.0000, 0.6773),
        /* USD */ doubleArrayOf(7.0274, 0.9219, 111.5819, 1.5753, 1.0000)
    )

    val data = CurrencyData(currencies, exchangeRates)

    // Algorithm
    println("Algorithm output:")
    val cycle = detectArbitrage(data)
    println("Cycle (${cycle.sumOf { it.negativeLog }}): ${cycle.joinToString(" -> ")}")
    println("Profit: ${cycle.map { it.rate }.reduce { acc, d -> acc * d }}")
}

fun detectArbitrage(data: CurrencyData): List<Edge> {
    val vertices = data.currencies.size
    val edges = data.edges.size

    val dist = DoubleArray(vertices) { Double.MAX_VALUE }
    dist[0] = 0.0

    val predecessors = IntArray(vertices) { -1 }

    for (i in 0 until vertices - 1) {
        for (j in 0 until edges) {
            val edge = data.edges[j]
            val from = edge.from.ordinal
            val to = edge.to.ordinal
            val weight = edge.negativeLog

            if (dist[from] != Double.MAX_VALUE && dist[from] + weight < dist[to]) {
                dist[to] = dist[from] + weight
                predecessors[to] = from
            }
        }
    }

    val possibleCycles = mutableListOf<List<Edge>>()

    for (i in 0 until edges) {
        val edge = data.edges[i]
        var from = edge.from.ordinal
        val to = edge.to.ordinal
        val weight = edge.negativeLog

        if (dist[from] != Double.MAX_VALUE && dist[from] + weight < dist[to]) {
            // Negative cycle exists
            predecessors[to] = from
            val cycle = mutableListOf(to, from)
            while (true) {
                from = predecessors[from]
                if (cycle.contains(from)) {
                    break
                }
                cycle += from
            }
            cycle += to
            cycle.reverse()

            val cycleEdges = mutableListOf<Edge>()
            for (j in 0 until cycle.size - 1) {
                cycleEdges += data.getEdge(cycle[j], cycle[j + 1])
            }
            if (cycleEdges.size > 2) {
                possibleCycles += cycleEdges
            }
        }
    }

    return possibleCycles.maxByOrNull { it.map { it.rate }.reduce { acc, d -> acc * d } } ?: emptyList()
}