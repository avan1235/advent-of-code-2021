import kotlin.math.absoluteValue
import kotlin.math.sign

object Day17 : AdventDay() {
  override fun solve() {
    val data = reads<String>()?.singleOrNull() ?: return
    val targetArea = data.toTargetArea()

    val maxX = targetArea.x.maxOf { it.absoluteValue }
    val maxY = targetArea.y.maxOf { it.absoluteValue }

    targetArea.runSimulations(x = -maxX..maxX, y = -maxY..maxY).run {
      maxOf { state -> state.yHistory.maxOf { it } }.printIt()
      size.printIt()
    }
  }
}

private fun String.toTargetArea() = removePrefix("target area: x=").split(", y=")
  .map { rng -> rng.split("..").let { (from, to) -> from.toInt() directedTo to.toInt() } }
  .let { (x, y) -> TargetArea(x, y) }

private data class TargetArea(val x: IntProgression, val y: IntProgression) {
  fun runSimulations(x: IntRange, y: IntRange): List<State> {
    return buildList {
      for (vx in x) for (vy in y) simulate(vx, vy)?.let { add(it) }
    }
  }

  fun simulate(vx: Int, vy: Int): State? {
    var state = State(vx, vy)
    while (state.canReach(this)) {
      state = state.step()
      if (state.x in x && state.y in y) return state
    }
    return null
  }
}

private data class State(
  val vx: Int, val vy: Int,
  val x: Int = 0, val y: Int = 0,
  val yHistory: List<Int> = listOf(),
) {
  fun step() = State(
    x = x + vx,
    y = y + vy,
    vx = vx - vx.sign,
    vy = vy - 1,
    yHistory = yHistory + y,
  )

  fun canReach(targetArea: TargetArea) = when {
    vy < 0 && y < targetArea.y.first -> false
    vx == 0 && x !in targetArea.x -> false
    else -> true
  }
}
