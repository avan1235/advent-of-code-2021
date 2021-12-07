import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

object Day7 : AdventDay() {
    override fun solve() {
        val positions = reads<String>()?.singleOrNull()
            ?.let { line -> line.split(",").map { it.value<Int>() } } ?: return

        val median = positions.median()
        val avg = positions.average().let { if (it > median) floor(it) else ceil(it) }.toInt()

        positions.distance(median) { from, to -> abs(from - to) }.printIt()
        (avg directedTo median).minOf {
            positions.distance(it) { from, to -> abs(from - to) * (abs(from - to) + 1) / 2 }
        }.printIt()
    }
}

private fun List<Int>.distance(from: Int, by: (Int, Int) -> Int) = sumOf { by(from, it) }

private fun Iterable<Int>.median() = sorted().run { (this[(size - 1) / 2] + this[size / 2]) / 2 }