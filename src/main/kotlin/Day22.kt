import kotlin.math.max
import kotlin.math.min

object Day22 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val steps = data.map { it.toStep() }

    LimitedReactor(limit = -50..50).apply { steps.forEach { execute(it) } }.size.printIt()
    Reactor().apply { steps.forEach { execute(it) } }.size.printIt()
  }
}

private fun String.toRange() = drop(2).split("..")
  .map { it.toInt() }.let { (f, t) -> f..t }

private fun String.toStep() = split(" ").let { (a, r) ->
  val (x, y, z) = r.split(",").map { it.toRange() }
  Step(Action.valueOf(a.uppercase()), Range3D(x, y, z))
}

private infix fun IntRange.limit(l: IntRange?) = l?.let { max(first, l.first)..min(last, l.last) } ?: this

private enum class Action { ON, OFF }
private data class Step(val action: Action, val range: Range3D) {
  fun cubes(l: IntRange? = null) = buildSet {
    for (xi in range.x limit l) for (yi in range.y limit l)
      for (zi in range.z limit l) add(Cube(xi, yi, zi))
  }
}

private data class Cube(val x: Int, val y: Int, val z: Int)
private class LimitedReactor(private val limit: IntRange) {
  private val on = hashSetOf<Cube>()
  val size get() = on.size

  fun execute(step: Step) = when (step.action) {
    Action.ON -> on += step.cubes(limit)
    Action.OFF -> on -= step.cubes(limit)
  }
}

private infix fun IntRange.outside(r: IntRange) = last < r.first || first > r.last
private infix fun IntRange.inside(r: IntRange) = first >= r.first && last <= r.last
private val IntRange.size get() = last - first + 1
private operator fun IntRange.minus(r: IntRange): Sequence<IntRange> = when {
  this inside r -> sequenceOf(this)
  r inside this -> sequenceOf(first..r.first - 1, r, r.last + 1..last)
  r outside this -> sequenceOf(this)
  last < r.last -> sequenceOf(first..r.first - 1, r.first..last)
  r.first < first -> sequenceOf(first..r.last, r.last + 1..last)
  else -> error("Not defined minus for $this-$r")
}.filter { it.size > 0 }

private class Reactor {
  private val on: HashSet<Range3D> = hashSetOf()
  val size get() = on.sumOf { it.size }

  fun execute(step: Step) = when (step.action) {
    Action.OFF -> on.flatMap { it - step.range }.toHashSet().also { on.clear() }
    Action.ON -> on.fold(hashSetOf(step.range)) { cut, curr -> cut.flatMap { it - curr }.toHashSet() }
  }.let { on += it }
}

private data class Range3D(val x: IntRange, val y: IntRange, val z: IntRange) {
  val size get() = x.size.toLong() * y.size.toLong() * z.size.toLong()

  operator fun minus(r: Range3D): Sequence<Range3D> =
    if (r outside this) sequenceOf(this)
    else sequence {
      for (x in x - r.x) for (y in y - r.y) for (z in z - r.z) yield(Range3D(x, y, z))
    }.filter { it inside this && it outside r }

  infix fun outside(r: Range3D) = x outside r.x || y outside r.y || z outside r.z
  infix fun inside(r: Range3D) = x inside r.x && y inside r.y && z inside r.z
}
