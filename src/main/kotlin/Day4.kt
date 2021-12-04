object Day4 : AdventDay() {
    override fun solve() {
        val lines = reads<String>() ?: return
        val game = Game(lines.extractOrder(), lines.extractBoards())

        game.simulateSelecting { board -> board.wins() }
            ?.let { (b, v) -> b.unmarkedValues().sum() * v }.printIt()

        val leftBoards = game.boards.toMutableSet()
        game.simulateSelecting { board ->
            if (board.wins()) leftBoards -= board
            leftBoards.isEmpty()
        }?.let { (b, v) -> b.unmarkedValues().sum() * v }.printIt()
    }

    private fun List<String>.extractOrder() = firstOrNull()?.split(",")?.map { it.value<Int>() }
        ?: throw IllegalArgumentException("No order defined in data: $this")

    private fun List<String>.extractBoards() = drop(1).groupDividedBy("") { it.toBoard<Int>() }
}

private class Game<V>(val order: List<V>, val boards: List<Board<V>>) {
    fun simulateSelecting(strategy: (Board<V>) -> Boolean): Pair<Board<V>, V>? {
        for (v in order) {
            boards.forEach { it.mark(v) }
            for (board in boards) {
                if (strategy(board)) return Pair(board, v)
            }
        }
        return null
    }
}

private inline fun <reified V> List<String>.toBoard() = map { line ->
    line.splitToSequence("\\s+".toRegex()).filter { it.isNotBlank() }.mapTo(mutableListOf()) { it.value<V>() }
}.let { Board(it) }

private class Board<V>(private val values: List<List<V>>) {
    private val transposedValues = values.transpose()
    private val markedValues = mutableSetOf<V>()
    private val allValues = values.flatten()

    fun mark(value: V) = markedValues.add(value)
    fun wins() = values.wins() || transposedValues.wins()
    fun unmarkedValues() = allValues - markedValues

    private fun List<List<V>>.wins() = any { row -> row.all { it in markedValues } }
}

fun <V> List<String>.groupDividedBy(separator: String, transform: (List<String>) -> V): List<V> = sequence {
    var curr = mutableListOf<String>()
    forEach { string ->
        when (separator) {
            string -> {
                if (curr.isNotEmpty()) yield(transform(curr))
                curr = mutableListOf()
            }
            else -> curr.add(string)
        }
    }
    if (curr.isNotEmpty()) yield(transform(curr))
}.toList()

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val n = map { it.size }.toSet().singleOrNull() ?: throw IllegalArgumentException("Invalid data to transpose: $this")
    return List(n) { y -> List(size) { x -> this[x][y] } }
}
