sealed class AdventDay(private val readFromStdIn: Boolean = false) {

    abstract fun solve()

    inline fun <reified T> reads() = getInputLines()?.map { it.value<T>() }

    fun getInputLines() =
        if (readFromStdIn) generateSequence { readLine() }.toList()
        else this::class.java.getResource("/input/${this::class.java.simpleName}.in")
            ?.openStream()?.bufferedReader()?.readLines()
}

inline fun <reified T> String.value(): T = when (T::class) {
    String::class -> this as T
    Long::class -> toLongOrNull() as T
    Int::class -> toIntOrNull() as T
    else -> TODO("Add support to read ${T::class.java.simpleName}")
}

fun Any?.printIt() = this.also { println(it) }

fun <U, V> List<U>.groupSeparatedBy(
    separator: U,
    transform: (List<U>) -> V
): List<V> = sequence {
    var curr = mutableListOf<U>()
    forEach {
        if (it == separator && curr.isNotEmpty()) yield(transform(curr))
        if (it == separator) curr = mutableListOf()
        else curr += it
    }
    if (curr.isNotEmpty()) yield(transform(curr))
}.toList()

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val n = map { it.size }.toSet().singleOrNull()
        ?: throw IllegalArgumentException("Invalid data to transpose: $this")
    return List(n) { y -> List(size) { x -> this[x][y] } }
}
