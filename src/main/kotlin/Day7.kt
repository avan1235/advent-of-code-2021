import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

object Day7 : AdventDay() {
    override fun solve() {
        val positions = reads<String>()?.singleOrNull()
            ?.separated<Int>(by = ",") ?: return

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

private fun List<Int>.absCost(from: Int) =
    sumOf { to -> abs(from - to) }

private fun List<Int>.incrCost(from: Int) =
    sumOf { to -> abs(from - to) * (abs(from - to) + 1) / 2 }

private fun List<Int>.median() = sorted()
    .run { (this[(size - 1) / 2] + this[size / 2]) / 2 }