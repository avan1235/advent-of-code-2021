object Day9 : AdventDay() {
  override fun solve() {
    val map = reads<String>()?.toMap() ?: return

    map.indices.asSequence()
      .filter { p -> map.neighbours(of = p).all { map[it] > map[p] } }
      .sumOf { map[it] + 1 }
      .printIt()

    map.indices.asSequence()
      .map { p -> map.search(from = p) { from, to -> map[from] < map[to] && map[to] != 9 } }
      .distinct().map { it.size }
      .sortedDescending().take(3)
      .fold(1, Int::times)
      .printIt()
  }
}

private fun List<String>.toMap() = Map(map { line -> line.map { it.digitToInt() } })

private data class Node(val x: Int, val y: Int)

private data class Map<V>(val heights: List<List<V>>) {

  val indices = heights.flatMapIndexed { y, row -> row.indices.map { Node(it, y) } }

  operator fun get(p: Node): V = with(p) { heights[y][x] }

  fun neighbours(of: Node) = with(of) {
    sequenceOf(
      Node(x + 1, y), Node(x - 1, y),
      Node(x, y + 1), Node(x, y - 1)
    ).filter { it.isValid() }
  }

  enum class SearchType { DFS, BFS }

  fun search(
    from: Node,
    type: SearchType = SearchType.DFS,
    action: (Node) -> Unit = {},
    edge: (Node, Node) -> Boolean = { _, _ -> true }
  ): Set<Node> {
    val visited = mutableSetOf<Node>()
    val queue = ArrayDeque<Node>()
    tailrec fun go(curr: Node) {
      visited += curr.also(action)
      neighbours(curr).filter { edge(curr, it) && it !in visited }.forEach { queue += it }
      when (type) {
        SearchType.DFS -> go(queue.removeLastOrNull() ?: return)
        SearchType.BFS -> go(queue.removeFirstOrNull() ?: return)
      }
    }
    return visited.also { go(from) }
  }

  private fun Node.isValid() = y in heights.indices && x in heights[y].indices
}
