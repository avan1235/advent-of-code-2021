import V3.Companion.TRANSFORMS
import kotlin.math.absoluteValue

object Day19 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val scanners = data.groupSeparatedBy(separator = { it == "" }) { it.toScanner() }

    val matcher = ScannersMatcher(scanners, minCommon = 12)
    val start = scanners.first()
    val (beaconsFromStart, positioned) = matcher.findPairing(start)

    beaconsFromStart.size.printIt()
    sequence {
      for ((s1, v1) in positioned) for ((s2, v2) in positioned)
        if (s1 != s2) yield(v1 - v2)
    }.maxOf { it.manhattanValue }.printIt()
  }
}

private class ScannersMatcher(val scanners: List<Scanner>, val minCommon: Int) {

  private data class FT(val from: Scanner, val to: Scanner)

  private val cachedPair = mutableMapOf<FT, V3.T>()
  private val triedToPair = DefaultMap<FT, Boolean>(false)

  fun findPairing(start: Scanner): Pair<Set<V3>, Map<Scanner, V3>> {
    val transform = DefaultMap<Scanner, List<V3.T>>(emptyList())
    val beacons = start.beacons.toMutableSet()
    val scan = mutableMapOf<Scanner, V3>().also { it[start] = V3.ZERO }

    val paired = mutableSetOf(start)
    val toPair = (scanners - paired).toMutableSet()

    while (toPair.isNotEmpty()) {
      search@ for (from in paired) for (to in toPair) {
        val pairedShift = tryPair(FT(from, to)) ?: continue
        transform[to] = transform[from] + pairedShift
        beacons += to.beacons.map { transform[to](it) }
        scan[to] = transform[to](V3.ZERO)
        to.also { paired += it }.also { toPair -= it }
        break@search
      }
    }
    return Pair(beacons, scan)
  }

  private fun tryPair(ft: FT): V3.T? {
    if (triedToPair[ft]) return cachedPair[ft]
    triedToPair[ft] = true
    for (t in TRANSFORMS) {
      val to = t(ft.to)
      val diffs = buildSet {
        for (fb in ft.from.beacons) for (tb in to.beacons) add(tb - fb)
      }
      for (diff in diffs) {
        val cnt = to.beacons.count { tb -> (tb - diff) in ft.from.beacons }
        if (cnt >= minCommon) return t.copy(shift = -diff).also { cachedPair[ft] = it }
      }
    }
    return null
  }
}

private fun List<String>.toScanner() = Scanner(
  first().removePrefix("--- scanner ").takeWhile { it.isDigit() }.toInt(),
  drop(1).map { it.toBeacon() }.toSet()
)

private fun String.toBeacon() = split(",").map { it.toInt() }
  .let { (x, y, z) -> V3(x, y, z) }

private data class V3(val x: Int, val y: Int, val z: Int) {
  data class T(val id: Int, val shift: V3)

  val manhattanValue = x.absoluteValue + y.absoluteValue + z.absoluteValue
  private fun axeRotated(id: Int) = when (id) {
    0 -> V3(x, y, z)
    1 -> V3(-y, x, z)
    2 -> V3(-x, -y, z)
    3 -> V3(y, -x, z)
    else -> error("Invalid axeRotate id")
  }

  private fun axeChanged(id: Int) = when (id) {
    0 -> V3(x, y, z)
    1 -> V3(x, z, -y)
    2 -> V3(x, -z, y)
    3 -> V3(x, -y, -z)
    4 -> V3(-z, y, x)
    5 -> V3(z, y, -x)
    else -> error("Invalid axeChanged id")
  }

  infix fun transformedBy(by: T) = axeChanged(by.id / 4).axeRotated(by.id % 4) + by.shift

  operator fun plus(v3: V3) = V3(x + v3.x, y + v3.y, z + v3.z)
  operator fun minus(v3: V3) = V3(x - v3.x, y - v3.y, z - v3.z)
  operator fun unaryMinus() = ZERO - this

  companion object {
    val ZERO = V3(0, 0, 0)
    val TRANSFORMS = (0..23).map { T(it, ZERO) }
  }
}

private class Scanner(val id: Int, val beacons: Set<V3>) {
  override fun equals(other: Any?) = (other as? Scanner)?.id == id
  override fun hashCode() = id
}

private operator fun List<V3.T>.invoke(v: V3) = foldRight(v) { t, v3 -> v3 transformedBy t }
private operator fun V3.T.invoke(s: Scanner) = Scanner(s.id, s.beacons.map { it transformedBy this }.toSet())
