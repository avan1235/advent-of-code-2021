object Day6 : AdventDay() {
    override fun solve() {
        val initShoal = reads<String>()?.singleOrNull()?.toFishShoal() ?: return

        (1..80).fold(initShoal) { shoal, _ -> shoal.afterDay() }.size.printIt()
        (1..256).fold(initShoal) { shoal, _ -> shoal.afterDay() }.size.printIt()
    }
}

private fun String.toFishShoal() = split(",").map { LanternFish(it.toInt()) }
    .groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    .let { FishShoal(it) }

private data class LanternFish(private val timer: Int) {
    fun afterDay(): List<LanternFish> = when (val nextTimer = timer - 1) {
        -1 -> listOf(LanternFish(6), LanternFish(8))
        else -> listOf(LanternFish(nextTimer))
    }
}

private class FishShoal(val counts: Map<LanternFish, Long>) {
    val size = counts.values.sum()

    fun afterDay(): FishShoal = DefaultMap<LanternFish, Long>(0).also {
        counts.forEach { (fish, count) ->
            fish.afterDay().forEach { newFish -> it[newFish] = it[newFish] + count }
        }
    }.let { FishShoal(it) }
}
