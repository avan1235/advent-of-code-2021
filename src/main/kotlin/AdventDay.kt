sealed class AdventDay(private val readFromStdIn: Boolean = false) {

  abstract fun solve()

  inline fun <reified T> reads() = getInputLines()?.map { it.value<T>() }

  fun getInputLines() =
    if (readFromStdIn) generateSequence { readLine() }.toList()
    else this::class.java.getResource("/input/${this::class.java.simpleName}.in")
      ?.openStream()?.bufferedReader()?.readLines()

  companion object {
    val all = AdventDay::class.sealedSubclasses
      .mapNotNull { it.objectInstance }
      .sortedBy { it::class.java.simpleName.removePrefix("Day").toInt() }
  }
}
