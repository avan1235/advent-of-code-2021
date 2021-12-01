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

fun Any?.printIt() = println(this)

