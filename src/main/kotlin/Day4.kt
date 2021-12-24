object Day4 : AdventDay() {
  override fun solve() {
    val lines = reads<String>() ?: return
    val game = Game(lines.extractOrder(), lines.extractBoards())

    game.simulateSelectingFirst { board -> board.wins() }
      ?.let { (b, v) -> b.unmarkedValues().sum() * v }.printIt()

    val leftBoards = game.boards.toMutableSet()
    game.simulateSelectingFirst { board ->
      if (board.wins()) leftBoards -= board
      leftBoards.isEmpty()
    }?.let { (b, v) -> b.unmarkedValues().sum() * v }.printIt()
  }

  private fun List<String>.extractOrder() =
    firstOrNull()?.split(",")?.map { it.value<Int>() }
      ?: throw IllegalArgumentException("No order defined in data: $this")

  private fun List<String>.extractBoards() =
    drop(1).groupSeparatedBy(separator = { it == "" }) { it.toBoard<Int>() }
}

private class Game<V>(val order: List<V>, val boards: List<Board<V>>) {
  fun simulateSelectingFirst(strategy: (Board<V>) -> Boolean): Pair<Board<V>, V>? {
    for (v in order) {
      boards.forEach { it.mark(v) }
      boards.firstOrNull(strategy)?.let { return Pair(it, v) }
    }
    return null
  }
}

private inline fun <reified V> List<String>.toBoard() = map { line ->
  line.splitToSequence("\\s+".toRegex())
    .filter { it.isNotBlank() }
    .mapTo(mutableListOf()) { it.value<V>() }
}.let { Board(it) }

private class Board<V>(private val values: List<List<V>>) {
  private val transposedValues = values.transpose()
  private val markedValues = mutableSetOf<V>()
  private val allValues = values.flatten()

  fun mark(value: V) = markedValues.add(value)
  fun wins() = values.rowWins() || transposedValues.rowWins()
  fun unmarkedValues() = allValues - markedValues

  private fun List<List<V>>.rowWins() =
    any { row -> row.all { it in markedValues } }
}
