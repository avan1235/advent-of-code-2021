object Day1 : AdventDay() {
    override fun solve() {
        val depths = reads<Int>() ?: return
        depths.asSequence().countIncreases().printIt()
        depths.asSequence().countSumIncreases().printIt()
    }

    private fun Sequence<Int>.countIncreases() = windowed(size = 2)
        .filter { (prev, curr) -> curr > prev }
        .count()

    private fun Sequence<Int>.countSumIncreases(size: Int = 3) = windowed(size)
        .map { it.sum() }
        .countIncreases()
}