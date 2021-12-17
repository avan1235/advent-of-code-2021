import kotlinx.coroutines.*
import kotlin.math.absoluteValue

object Day17 : AdventDay() {
  override fun solve() {
    val data = reads<String>()?.singleOrNull() ?: return
    val targetArea = data.toTargetArea()

    val maxX = maxOf(targetArea.x.maxOf { it.absoluteValue })
    val maxY = maxOf(targetArea.y.maxOf { it.absoluteValue })

    val states = targetArea.runSimulations(x = -maxX..maxX, y = -maxY..maxY)
    states.maxOf { state -> state.yHistory.maxOf { it } }.printIt()
    states.size.printIt()
  }
}

private fun String.toTargetArea() = removePrefix("target area: x=").split(", y=")
  .map { rng -> rng.split("..").let { (from, to) -> from.toInt() directedTo to.toInt() } }
  .let { (x, y) -> TargetArea(x, y) }

private data class TargetArea(val x: IntProgression, val y: IntProgression) {
  operator fun contains(state: State) = state.x in x && state.y in y

  fun runSimulations(x: IntRange, y: IntRange): List<State> {
    val jobs = buildList {
      for (vx in x) for (vy in y) CoroutineScope(Dispatchers.IO)
        .async { simulate(vx, vy) }
        .let { add(it) }
    }
    return runBlocking { jobs.awaitAll() }.filterNotNull()
  }

  fun simulate(vx: Int, vy: Int): State? {
    var state = State(vx, vy)
    while (state.canReach(this)) {
      state = state.step()
      if (state in this) return state
    }
    return null
  }
}

private data class State(
  val vx: Int, val vy: Int,
  val x: Int = 0, val y: Int = 0,
  val xHistory: List<Int> = listOf(), val yHistory: List<Int> = listOf(),
) {
  fun step() = State(
    x = x + vx,
    y = y + vy,
    vx = if (vx > 0) vx - 1 else if (vx < 0) vx + 1 else 0,
    vy = vy - 1,
    xHistory = xHistory + x,
    yHistory = yHistory + y,
  )

  fun canReach(targetArea: TargetArea) = when {
    vy < 0 && y < targetArea.y.first -> false
    vx == 0 && x !in targetArea.x -> false
    else -> true
  }
}
