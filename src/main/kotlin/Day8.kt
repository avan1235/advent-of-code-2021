object Day8 : AdventDay() {
    override fun solve() {
        val positions = reads<String>()?.map { it.toDigitsEntry() } ?: return

        positions.sumOf { it.outputs.count(Digit::isEasy) }.printIt()
        positions.sumOf { it.decode() }.printIt()
    }
}

private fun String.toDigitsEntry() = split(" | ").map { part ->
    part.split(" ").map { Digit(it.toSet()) }
}.let { (input, output) -> DigitsEntry(input, output) }

private data class Digit(val segments: Set<Char>) {
    val isEasy = segments.size in setOf(2, 3, 4, 7)
    operator fun minus(o: Digit) = Digit(segments - o.segments)
}

private data class DigitsEntry(val inputs: List<Digit>, val outputs: List<Digit>) {
    fun decode(): Int = deduce().let { enc ->
        outputs.fold(0) { acc, dig -> 10 * acc + enc[dig]!! }
    }

    private fun deduce(): Map<Digit, Int> {
        val seg = inputs.toSet().groupBy { it.segments.size }
        val one = seg[2]!!.single()
        val four = seg[4]!!.single()
        val seven = seg[3]!!.single()
        val eight = seg[7]!!.single()

        fun MutableSet<Digit>.extract(by: Digit, diff: Int) =
            single { (it - by).segments.size == diff }.also { this -= it }

        val fiveSeg = seg[5]!!.toMutableSet()
        val three = fiveSeg.extract(one, 3)
        val two = fiveSeg.extract(four, 3)
        val five = fiveSeg.single()

        val sixSeg = seg[6]!!.toMutableSet()
        val nine = sixSeg.extract(four, 2)
        val six = sixSeg.extract(one, 5)
        val zero = sixSeg.single()

        return listOf(zero, one, two, three, four, five, six, seven, eight, nine)
            .zip(0..9).toMap()
    }
}