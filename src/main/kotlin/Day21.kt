object Day21 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val (p1, p2) = data.map { it.toPlayer() }

    simulateGame(p1, p2)?.let { (p, idx) -> p.points * idx }.printIt()
    simulateQuantumGame(p1, p2).maxOf { it.value }.printIt()
  }
}

private fun simulateGame(p1: Player, p2: Player): Pair<Player, Int>? {
  generateDiceNumbers().foldIndexed(DiceGame(p1, p2, toPoints = 1000)) { idx, game, dice ->
    game.move(dice).apply { looser()?.let { return Pair(it, 3 * (idx + 1)) } }
  }
  return null
}

private val QUANTUM_DICE_SPLITS = listOf(
  3 to 1, // 1+1+1
  4 to 3, // 1+1+2, 1+2+1, 2+1+1,
  5 to 6, // 2+2+1, 2+1+2, 1+2+2, 1+1+3, 1+3+1, 3+1+1
  6 to 7, // 1+2+3, 1+3+2, 2+1+3, 2+3+1, 3+1+2, 3+2+1, 2+2+2
  7 to 6, // 2+2+3, 2+3+2, 3+2+2, 3+3+1, 3+1+3, 1+3+3
  8 to 3, // 3+3+2, 3+2+3, 2+3+3
  9 to 1, // 3+3+3
)

private fun simulateQuantumGame(p1: Player, p2: Player): Map<Int, Long> {
  val playing = mapOf(DiceGame(p1, p2, toPoints = 21) to 1L).toDefaultMap(0)
  val winCount = DefaultMap<Int, Long>(0L)

  while (playing.isNotEmpty()) {
    val updated = DefaultMap<DiceGame, Long>(0)
    for (game in playing.keys) {
      for ((dice, splits) in QUANTUM_DICE_SPLITS) {
        val nextGame = game.move(dice)
        when (val winner = nextGame.winner()) {
          null -> updated[nextGame] = updated[nextGame] + splits * playing[game]
          else -> winCount[winner.idx] = winCount[winner.idx] + splits * playing[game]
        }
      }
    }
    playing.also { it.clear() }.also { it.putAll(updated) }
  }
  return winCount
}

private fun generateDiceNumbers() = generateSequence(0) { it + 1 }
  .map { it % 100 + 1 }.windowed(size = 3, step = 3) { it.sum() }

private fun String.toPlayer() = removePrefix("Player ").run {
  val idx = takeWhile { it.isDigit() }.toInt()
  val position = dropWhile { it.isDigit() }.removePrefix(" starting position: ").toInt()
  Player(idx, position, points = 0)
}

private data class Player(val idx: Int, val position: Int, val points: Long) {
  fun move(rolled: Int): Player = ((position - 1 + rolled) % 10 + 1).let { copy(position = it, points = it + points) }
}

private data class DiceGame(val now: Player, val last: Player, val toPoints: Long) {
  fun move(rolled: Int): DiceGame = copy(now = last, last = now.move(rolled))
  fun looser(): Player? = if (last.points >= toPoints) now else null
  fun winner(): Player? = if (last.points >= toPoints) last else null
}
