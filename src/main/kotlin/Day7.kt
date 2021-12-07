import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

object Day7 : AdventDay(readFromStdIn = true) {
    override fun solve() {
        val positions = reads<String>()?.singleOrNull()
            ?.let { line -> line.split(",").map { it.value<Int>() } } ?: return

        positions.median()
            .let { positions.absCost(it) }
            .printIt()

        positions.average()
            .let { sequenceOf(floor(it), ceil(it)) }
            .map { it.toInt() }
            .minOf { positions.incrCost(it) }
            .printIt()
    }
}

private fun List<Int>.absCost(pos: Int) = distance(pos) { from, to -> abs(from - to) }

private fun List<Int>.incrCost(pos: Int) = distance(pos) { from, to -> abs(from - to) * (abs(from - to) + 1) / 2 }

private fun List<Int>.distance(from: Int, by: (Int, Int) -> Int) = sumOf { by(from, it) }

private fun List<Int>.median() = sorted().run { (this[(size - 1) / 2] + this[size / 2]) / 2 }