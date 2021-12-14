object Day14 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return

    val polymer = data.firstOrNull()?.toPolymer() ?: return
    val rules = data.toInsertionRules()

    polymer.apply(rules, times = 10).stats().printIt()
    polymer.apply(rules, times = 40).stats().printIt()
  }
}

private fun String.toPolymer() = windowed(2).groupingBy { it }.eachCount()
  .mapValues { it.value.toLong() }.let { Polymer(it, first(), last()) }

private fun List<String>.toInsertionRules() = buildMap {
  this@toInsertionRules.drop(2).forEach { line ->
    line.split(" -> ").let { (from, to) ->
      put(from, listOf(from.first() + to, to + from.last()))
    }
  }
}.let { InsertionRules(it) }

private data class Polymer(val counts: Map<String, Long>, val first: Char, val last: Char) {

  fun apply(rules: InsertionRules, times: Int) = (1..times).fold(this) { p, _ -> rules(p) }

  fun stats() = counts().run { maxOf { it.value } - minOf { it.value } }

  fun counts() = DefaultMap<Char, Long>(0).apply {
    counts.forEach { (p, cnt) -> p.forEach { this[it] = this[it] + cnt } }
    this[first] = this[first] + 1
    this[last] = this[last] + 1
  }.run { mapValues { it.value / 2 } }
}

private data class InsertionRules(val change: Map<String, List<String>>) {

  operator fun invoke(polymer: Polymer): Polymer = DefaultMap<String, Long>(0).apply {
    polymer.counts.forEach { (pattern, count) ->
      (change[pattern] ?: listOf(pattern)).forEach { this[it] = this[it] + count }
    }
  }.let { polymer.copy(counts = it) }
}
