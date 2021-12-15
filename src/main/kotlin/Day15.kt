import java.util.*

object Day15 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return

    data.toWeightedGraph(times = 1).shortestPathLength().printIt()
    data.toWeightedGraph(times = 5).shortestPathLength().printIt()
  }
}

private fun List<String>.toWeightedGraph(times: Int): WeightedGraph = map { line ->
  line.mapNotNull { it.digitToIntOrNull() }
}.let { data ->
  val m = data.first().size
  val n = data.size
  val md = m * times
  val nd = n * times

  LazyDefaultMap<N, MutableList<E>>(::mutableListOf).also { adj ->
    fun isOnMap(x: Int, y: Int) = x in 0 until md && y in 0 until md
    for (x in 0 until md) for (y in 0 until nd)
      for ((xc, yc) in listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)) {
        if (!isOnMap(x + xc, y + yc)) continue
        val tx = x + xc
        val ty = y + yc
        adj[x o y] += E(tx o ty, (data[ty % n][tx % m] + (ty / n) + (tx / m) - 1) % 9 + 1)
      }
  }.let { WeightedGraph(md, nd, it) }
}

private data class N(val x: Int, val y: Int)
private data class E(val to: N, val w: Int)

private infix fun Int.o(v: Int) = N(this, v)

private class WeightedGraph(val m: Int, val n: Int, private val adj: Map<N, List<E>>) {

  fun shortestPathLength(source: N = N(0, 0), dest: N = N(m - 1, n - 1)): Long {
    data class QN(val n: N, val dist: Long)

    val dist = DefaultMap<N, Long>(0)
    val queue = PriorityQueue(compareBy(QN::dist))
    adj.keys.forEach { v ->
      if (v != source) dist[v] = Long.MAX_VALUE
      queue.add(QN(v, dist[v]))
    }

    while (queue.isNotEmpty()) {
      val u = queue.remove()
      adj[u.n]?.forEach { edge ->
        val alt = dist[u.n] + edge.w
        if (alt >= dist[edge.to]) return@forEach
        dist[edge.to] = alt
        queue.add(QN(edge.to, alt))
      }
    }
    return dist[dest]
  }
}
