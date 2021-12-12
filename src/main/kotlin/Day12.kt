object Day12 : AdventDay() {
  override fun solve() {
    val graph = reads<String>()?.toGraph() ?: return

    graph.allPaths(Cave("start"), Cave("end")).size.printIt()
    graph.allPaths(Cave("start"), Cave("end"), allowTwice = true).size.printIt()
  }
}

private fun List<String>.toGraph() = map { line ->
  line.split("-").map { Cave(it) }.let { (f, s) -> Pair(f, s) }
}.let { Graph(it) }

@JvmInline
private value class Cave(val name: String) {
  fun isBig() = name.any { it.isUpperCase() }
  override fun toString(): String = name
}

private class Graph(edges: List<Pair<Cave, Cave>>) {

  private val adj = (edges + edges.map { Pair(it.second, it.first) })
    .groupBy(keySelector = { it.first }, valueTransform = { it.second })
    .mapValues { it.value.toSet() }

  fun allPaths(from: Cave, to: Cave, allowTwice: Boolean = false): Set<List<Cave>> {
    val reached = mutableSetOf<List<Cave>>()
    fun dfs(curr: Cave, path: List<Cave>, visited: DefaultMap<Cave, Int>, canVisitAgain: Boolean) {
      val currPath = path + curr
      if (curr == to) currPath.also { reached += it }.also { return }

      val currVisited = if (curr.isBig()) visited else visited + (curr to visited[curr] + 1)
      adj[curr]?.asSequence()
        ?.filter { visited[it] == 0 || (canVisitAgain && visited[it] == 1) }
        ?.filterNot { it == from }
        ?.forEach { dfs(it, currPath, currVisited, if (visited[it] == 1) false else canVisitAgain) }
    }
    return reached.also { dfs(from, emptyList(), DefaultMap(0), allowTwice) }
  }
}
