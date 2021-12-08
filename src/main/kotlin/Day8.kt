object Day8 : AdventDay() {
    override fun solve() {
        val positions = reads<String>()?.map { it.toDigitsEntry() } ?: return

        positions.sumOf { position ->
            position.outputs.count { it.segments.size in setOf(2, 3, 4, 7) }
        }.printIt()

        positions.sumOf { entry ->
            val encoding = entry.decode()
            entry.outputs.fold(0L) { acc, dig ->
                10L * acc + encoding[dig]!!
            }
        }.printIt()
    }
}

private fun String.toDigitsEntry() = split(" | ").map { part ->
    part.split(" ").map { Digit(it.toSet()) }
}.let { (input, output) -> DigitsEntry(input, output) }

private data class Digit(val segments: Set<Char>)

private operator fun Digit.minus(o: Digit) = Digit(segments - o.segments)

private data class DigitsEntry(val inputs: List<Digit>, val outputs: List<Digit>) {
    fun decode(): Map<Digit, Int> {
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