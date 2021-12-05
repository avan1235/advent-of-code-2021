import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day5 : AdventDay() {
    override fun solve() {
        val lines = reads<String>()?.map { it.toLine() } ?: return

        Diagram().apply {
            lines.filter { it.isVertical || it.isHorizontal }.forEach { markLine(it) }
            marked.count { it.value > 1 }.printIt()
        }
        Diagram().apply {
            lines.forEach { markLine(it) }
            marked.count { it.value > 1 }.printIt()
        }
    }
}

private data class P(val x: Int, val y: Int)
private data class Line(val from: P, val to: P) {
    val isHorizontal = from.x == to.x
    val isVertical = from.y == to.y
    val isDiagonal = abs(from.y - to.y) == abs(from.x - to.x)
}

private fun String.toLine() = split(" -> ").map { p ->
    p.split(",").let { (x, y) -> P(x.toInt(), y.toInt()) }
}.let { (f, t) -> Line(f, t) }

private class Diagram {
    private val _m = DefaultMap<P, Int>(0)
    val marked: Map<P, Int> get() = _m

    fun markLine(line: Line) = with(line) {
        when {
            isVertical -> (from.x range to.x).map { P(it, to.y) }.forEach { _m[it] = _m[it] + 1 }
            isHorizontal -> (from.y range to.y).map { P(to.x, it) }.forEach { _m[it] = _m[it] + 1 }
            isDiagonal -> (from.x range to.x).zip(from.y range to.y).map { (x, y) -> P(x, y) }.forEach { _m[it] = _m[it] + 1 }
            else -> Unit
        }
    }
}
