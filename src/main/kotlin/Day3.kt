object Day3 : AdventDay() {
    override fun solve() {
        val numbers = reads<String>() ?: return
        val n = numbers.commonLength()

        val zerosOnes = numbers.countZerosOnes(n)
        val gammaRate = zerosOnes.calcRate { zeros, ones -> ones > zeros }
        val epsilonRate = zerosOnes.calcRate { zeros, ones -> ones < zeros }
        (gammaRate * epsilonRate).printIt()

        val o2Rating = numbers.calculateRating(n) { zeros, ones -> zeros <= ones }
        val co2Rating = numbers.calculateRating(n) { zeros, ones -> zeros > ones }
        (o2Rating * co2Rating).printIt()
    }

    private fun List<String>.commonLength() = map { it.length }.toSet().singleOrNull()
        ?: throw IllegalArgumentException("No common length for list of strings: $this")

    private fun List<String>.countZerosOnes(n: Int) = listOf('0', '1')
        .map { c -> List(n) { idx -> count { it[idx] == c } } }
        .let { (zeros, ones) -> zeros.zip(ones) }

    private fun List<Pair<Int, Int>>.calcRate(predicate: (Int, Int) -> Boolean) = map { (zeros, ones) ->
        if (predicate(zeros, ones)) '1' else '0'
    }.joinToString("").toInt(radix = 2)

    private fun List<String>.calculateRating(n: Int, predicate: (Int, Int) -> Boolean): Int = toMutableList().apply {
        for (idx in 0 until n) {
            if (size == 1) break
            val (zeros, ones) = countZerosOnes(n)[idx]
            val commonValue = if (predicate(zeros, ones)) '1' else '0'
            removeIf { it[idx] != commonValue }
        }
    }.single().toInt(radix = 2)
}

