object Day25 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val sea = data.toSea()

    generateSequence(sea) { it.step().takeIf { update -> update != it } }.count().printIt()
  }
}

private fun List<String>.toSea(): Sea {
  val east = HashSet<Region>()
  val south = HashSet<Region>()
  val empty = HashSet<Region>()
  for ((y, line) in withIndex()) for ((x, c) in line.withIndex()) when (c) {
    '.' -> empty
    '>' -> east
    'v' -> south
    else -> error("Unknown input char: $c")
  } += Region(x, y)
  return Sea(east, south, empty, first().length, size)
}

private data class Region(val x: Int, val y: Int)

private data class Sea(
  val east: Set<Region>, val south: Set<Region>, val empty: Set<Region>,
  val xSize: Int, val ySize: Int,
) {
  fun step(): Sea {
    val (currEmpty, east) = moveGroup(empty, east) { east() }
    val (finalEmpty, south) = moveGroup(currEmpty, south) { south() }
    return copy(east = east, south = south, empty = finalEmpty)
  }

  private fun moveGroup(currEmpty: Set<Region>, moving: Set<Region>, move: Region.() -> Region) =
    HashSet(currEmpty).let { empty ->
      empty to moving.mapTo(HashSet()) { region ->
        region.move().takeIf { it in currEmpty }
          ?.also { empty -= it }
          ?.also { empty += region }
          ?: region
      }
    }

  private fun Region.east() = Region((x + 1) % xSize, y)
  private fun Region.south() = Region(x, (y + 1) % ySize)
}
