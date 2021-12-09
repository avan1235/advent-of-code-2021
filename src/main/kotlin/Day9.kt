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

private data class Loc(val x: Int, val y: Int)

private data class Map<V>(val heights: List<List<V>>) {

    val indices = heights.flatMapIndexed { y, row -> row.indices.map { Loc(it, y) } }

    operator fun get(p: Loc): V = with(p) { heights[y][x] }

    fun neighbours(of: Loc) = with(of) {
        sequenceOf(
            Loc(x + 1, y), Loc(x - 1, y),
            Loc(x, y + 1), Loc(x, y - 1)
        ).filter { it.isValid() }
    }

    enum class SearchType { DFS, BFS }

    fun search(
        from: Loc,
        type: SearchType = SearchType.DFS,
        action: (Loc) -> Unit = {},
        visit: (Loc, Loc) -> Boolean = { _, _ -> true }
    ): Set<Loc> {
        val visited = mutableSetOf<Loc>()
        val queue = ArrayDeque<Loc>()
        tailrec fun go(curr: Loc) {
            visited += curr.also(action)
            neighbours(curr).filter { visit(curr, it) && it !in visited }.forEach { queue += it }
            when (type) {
                SearchType.DFS -> go(queue.removeLastOrNull() ?: return)
                SearchType.BFS -> go(queue.removeFirstOrNull() ?: return)
            }
        }
        return visited.also { go(from) }
    }

    private fun Loc.isValid() = y in heights.indices && x in heights[y].indices
}