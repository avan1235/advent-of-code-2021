object Day9 : AdventDay() {
    override fun solve() {
        val map = reads<String>()?.toMap() ?: return

        map.indices.asSequence()
            .filter { p -> map.neighbours(of = p).all { map[it] > map[p] } }
            .sumOf { map[it] + 1 }
            .printIt()

        map.indices.asSequence()
            .map { p -> map.dfs(start = p) { from, to -> map[from] < map[to] && map[to] != 9 } }
            .distinct().map { it.size }
            .sortedDescending().take(3)
            .fold(1, Int::times)
            .printIt()
    }
}

private fun List<String>.toMap() = Map(map { line -> line.map { it.digitToInt() } })

private data class Pos(val x: Int, val y: Int)

private data class Map<V>(val heights: List<List<V>>) {

    val indices = heights.flatMapIndexed { y, row -> row.indices.map { Pos(it, y) } }

    operator fun get(p: Pos): V = with(p) { heights[y][x] }

    fun neighbours(of: Pos) = with(of) {
        sequenceOf(
            Pos(x + 1, y), Pos(x - 1, y),
            Pos(x, y + 1), Pos(x, y - 1)
        ).filter { it.isValid() }
    }

    fun dfs(start: Pos, visit: (Pos, Pos) -> Boolean): Set<Pos> {
        val visited = mutableSetOf<Pos>()
        val queue = ArrayDeque<Pos>()
        tailrec fun go(from: Pos) {
            visited += from
            neighbours(from).filterNot { it in visited }
                .filter { visit(from, it) }
                .forEach { queue += it }
            go(queue.removeLastOrNull() ?: return)
        }
        return visited.also { go(start) }
    }

    private fun Pos.isValid() = y in heights.indices && x in heights[y].indices
}