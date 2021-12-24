import java.util.*
import kotlin.collections.ArrayDeque

object Day23 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val extraLines = listOf("  #D#C#B#A#", "  #D#B#A#C#")

    data.findMinEnergy(maxRow = 3).printIt()
    data.addExtraLines(extraLines, startFrom = 3)
      .findMinEnergy(maxRow = 3 + extraLines.size).printIt()
  }
}

private fun List<String>.addExtraLines(lines: List<String>, startFrom: Int) = buildList {
  this@addExtraLines.take(startFrom).forEach { add(it) }
  lines.forEach { add(it) }
  this@addExtraLines.drop(startFrom).forEach { add(it) }
}

private class WayDescription(val collides: Set<F>, val steps: Int)
private class MapDescription(val spaces: Set<F>, val collides: DefaultMap<F, DefaultMap<F, WayDescription>>)

private fun List<String>.toMapDescription(maxRow: Int): MapDescription {
  val map = DefaultMap<F, ModelField>(ModelField.Wall).also { map ->
    forEachIndexed { y, line ->
      line.forEachIndexed { x, c -> F(x, y, maxRow).also { map[it] = c.toModelField(it) } }
    }
  }
  val spaces = map.entries.filter { it.value == ModelField.Used }.mapTo(HashSet()) { it.key }
  val collides = spaces
    .associateWith { scanPaths(start = it, spaces - it, map) }
    .toDefaultMap(DefaultMap(WayDescription(emptySet(), steps = 0)))

  return MapDescription(spaces, collides)
}

private fun List<String>.toMapState(maxRow: Int) = buildMap {
  forEachIndexed { y, line ->
    line.forEachIndexed { x, c ->
      F(x, y, maxRow).takeIf { it.isFinalPlace }?.let { put(it, AmphiodType.valueOf("$c")) }
    }
  }
}.let { MapState(it) }

private fun scanPaths(start: F, positions: Set<F>, map: Map<F, ModelField>): DefaultMap<F, WayDescription> {
  val path = DefaultMap<F, WayDescription>(WayDescription(emptySet(), 0))
  val visited = hashSetOf<F>()
  val queue = ArrayDeque<F>().also { it += start }
  tailrec fun go(curr: F) {
    curr.also { visited += it }.neighbours()
      .filterNot { it in visited }
      .filter { map[it] == ModelField.Used || map[it] == ModelField.Space }
      .onEach { queue += it }
      .forEach {
        path[it] = WayDescription(
          if (curr in positions) path[curr].collides + curr
          else path[curr].collides,
          steps = path[curr].steps + 1
        )
      }
    go(queue.removeFirstOrNull() ?: return)
  }
  return path.also { go(start) }
}

private fun List<String>.findMinEnergy(maxRow: Int): Long? {
  data class Reached(val state: MapState, val energy: Long)

  val mapDescription = toMapDescription(maxRow)
  val mapState = toMapState(maxRow)

  val dist = DefaultMap<MapState, Long>(Long.MAX_VALUE).also { it[mapState] = 0 }
  val queue = PriorityQueue(compareBy(Reached::energy)).also { it += Reached(mapState, 0) }

  while (queue.isNotEmpty()) {
    val curr = queue.remove()
    if (curr.state.isFinal) return dist[curr.state]

    curr.state.reachable(mapDescription).forEach neigh@{ (to, energy) ->
      val alt = dist[curr.state] + energy
      if (alt >= dist[to]) return@neigh
      dist[to] = alt
      queue += Reached(to, alt)
    }
  }
  return null
}

private data class MapStateChange(val mapState: MapState, val energy: Int)

private data class MapState(val positions: Map<F, AmphiodType>) {
  val isFinal by lazy { positions.all { it.value.col == it.key.x } }
  val byX: LazyDefaultMap<Int, HashSet<AmphiodType>> by lazy {
    LazyDefaultMap<Int, HashSet<AmphiodType>>(::hashSetOf).apply {
      positions.forEach { (f, type) -> this[f.x] += type }
    }
  }

  fun reachable(mapDescription: MapDescription) = if (isFinal) emptySequence() else sequence {
    val freeSpaces = mapDescription.spaces - positions.keys
    for ((from, type) in positions) {
      val otherPositions = HashMap(positions).also { it -= from }
      for (moveTo in freeSpaces) {
        if (!(from.isHallway xor moveTo.isHallway)) continue
        if (from.isHallway && type.col != moveTo.x) continue
        if (moveTo.isFinalPlace && byX[moveTo.x].any { it != type }) continue
        if (from.isFinalPlace && from.x == type.col && byX[from.x].all { it == type }) continue

        val onWay = mapDescription.collides[from][moveTo]
        if ((onWay.collides - freeSpaces).isNotEmpty()) continue

        val updatedMap = MapState(HashMap(otherPositions).also { it[moveTo] = type })
        yield(MapStateChange(updatedMap, onWay.steps * type.energy))
      }
    }
  }
}

private enum class ModelField { Wall, Space, Used }
private enum class AmphiodType(val energy: Int, val col: Int) {
  A(1, 3), B(10, 5), C(100, 7), D(1000, 9)
}

private data class F(val x: Int, val y: Int, private val maxRow: Int) {
  val isHallway = y == 1
  val isFinalColumn = x in FINAL_COLUMNS
  val isFinalRow = y in 2..maxRow
  val isFinalPlace = isFinalRow && isFinalColumn

  fun neighbours() = sequenceOf(f(x + 1, y), f(x, y - 1), f(x - 1, y), f(x, y + 1))
  private fun f(x: Int, y: Int) = copy(x = x, y = y)

  companion object {
    private val FINAL_COLUMNS = AmphiodType.values().map { it.col }.toHashSet()
  }
}

private fun Char.toModelField(f: F) = when {
  this == '#' || this == ' ' -> ModelField.Wall
  f.isFinalColumn && f.isHallway -> ModelField.Space
  else -> ModelField.Used
}
