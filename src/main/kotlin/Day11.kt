object Day11 : AdventDay() {
  override fun solve() {
    val map = reads<String>()?.toEnergyMap() ?: return

    with(map.copy()) {
      (1..100).fold(0) { sum, _ -> sum + simulateStep() }.printIt()
    }
    with(map.copy()) {
      generateSequence(1, Int::inc).first { simulateStep() == 100 }.printIt()
    }
  }
}

private fun List<String>.toEnergyMap() =
  EnergyMap(maxVal = 10, map { line -> line.map { it.digitToInt() }.toMutableList() })

private data class Pos(val x: Int, val y: Int)

private data class EnergyMap(val maxVal: Int, private val values: List<MutableList<Int>>) {

  val indices = values.flatMapIndexed { y, row -> row.indices.map { Pos(it, y) } }

  private val posOf = LazyDefaultMap(::mutableSetOf,
    indices.groupBy { this[it] }.mapValues { it.value.toMutableSet() }.toMutableMap()
  )

  fun copy() = EnergyMap(maxVal, values.map { it.toMutableList() })

  operator fun get(p: Pos): Int = with(p) { values[y][x] }
  operator fun set(p: Pos, v: Int) {
    val newVal = v.coerceAtMost(maxVal)
    posOf[this[p]].remove(p)
    posOf[newVal].add(p)
    values[p.y][p.x] = newVal
  }

  fun neighbours(of: Pos) = sequence { for (x in -1..1) for (y in -1..1) yield(Pair(x, y)) }
    .filter { (x, y) -> x != 0 || y != 0 }
    .map { (x, y) -> Pos(of.x + x, of.y + y) }
    .filter { it.isValid() }

  fun simulateStep(): Int {
    indices.forEach { this[it] = this[it] + 1 }
    val flashed = mutableSetOf<Pos>()
    while (true) {
      val nowFlashed = (posOf[maxVal] - flashed).takeIf { it.isNotEmpty() } ?: break
      flashed += nowFlashed
      nowFlashed.flatMap { neighbours(it) }.forEach { this[it] = this[it] + 1 }
    }
    indices.forEach { this[it] = this[it] % maxVal }
    return flashed.size
  }

  private fun Pos.isValid() = y in values.indices && x in values[y].indices
}
