import V3.Companion.TRANSFORMS
import kotlin.math.absoluteValue

object Day19 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val scanners = data.groupSeparatedBy("") { it.toScanner() }

    val matcher = ScannersMatcher(scanners, minCommon = 12)
    val start = scanners.first()
    val (beaconsFromStart, positioned) = matcher.findPairing(start)

    beaconsFromStart.size.printIt()
    sequence {
      for ((k1, v1) in positioned) for ((k2, v2) in positioned)
        if (k1 != k2) yield(v1 - v2)
    }.maxOf { it.manhattanValue }.printIt()
  }
}

private class ScannersMatcher(val scanners: List<Scanner>, val minCommon: Int) {

  private data class FT(val fromId: Int, val toId: Int)

  private val cachedPair = mutableMapOf<FT, V3.T>()
  private val triedToPair = DefaultMap<FT, Boolean>(false)

  fun findPairing(from: Scanner): Pair<Set<V3>, Map<Int, V3>> {
    val transform = DefaultMap<Int, List<V3.T>>(emptyList())
    val beacons = from.beacons.toMutableSet()
    val scan = mutableMapOf<Int, V3>().also { it[from.id] = V3.ZERO }

    val paired = hashSetOf(from.id)
    val toPair = (scanners.map { it.id } - paired).toHashSet()

    while (toPair.isNotEmpty()) {
      search@ for (fromId in paired) for (toId in toPair) {
        val pairedShift = tryPair(FT(fromId, toId)) ?: continue
        transform[toId] = transform[fromId] + pairedShift
        beacons += scanners[toId].beacons.map { it transformBy transform[toId] }
        scan[toId] = V3.ZERO transformBy transform[toId]
        toId.also { paired += it }.also { toPair -= it }
        break@search
      }
    }
    return Pair(beacons, scan)
  }

  private fun tryPair(ft: FT): V3.T? {
    if (triedToPair[ft]) return cachedPair[ft]
    triedToPair[ft] = true
    val from = scanners[ft.fromId]
    for (t in TRANSFORMS) {
      val to = scanners[ft.toId] transformBy t
      val shs = buildSet {
        for (fb in from.beacons) for (tb in to.beacons) add(fb - tb)
      }
      for (sh in shs) {
        val cnt = to.beacons.count { vb -> (vb + sh) in from.beacons }
        if (cnt >= minCommon) return t.copy(shift = sh).also { cachedPair[ft] = it }
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

  infix fun transformBy(by: T) = axeChanged(by.id / 4).axeRotated(by.id % 4) + by.shift
  infix fun transformBy(by: List<T>) = by.foldRight(this) { t, v3 -> v3 transformBy t }

  operator fun plus(v3: V3) = V3(x + v3.x, y + v3.y, z + v3.z)
  operator fun minus(v3: V3) = V3(x - v3.x, y - v3.y, z - v3.z)

  companion object {
    val ZERO = V3(0, 0, 0)
    val TRANSFORMS = (0..23).map { T(it, ZERO) }
  }
}

private class Scanner(val id: Int, val beacons: Set<V3>) {
  infix fun transformBy(t: V3.T) = Scanner(id, beacons.map { it transformBy t }.toHashSet())
  override fun equals(other: Any?) = (other as? Scanner)?.id == id
  override fun hashCode() = id
}
