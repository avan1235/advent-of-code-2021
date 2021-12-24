import java.io.ByteArrayOutputStream
import java.io.PrintStream

inline fun <reified T> String.value(): T = when (T::class) {
  String::class -> this as T
  Long::class -> toLongOrNull() as T
  Int::class -> toIntOrNull() as T
  else -> TODO("Add support to read ${T::class.java.simpleName}")
}

inline fun <reified T> String.separated(by: String): List<T> = split(by).map { it.value() }

fun <T> T.printIt() = also { println(it) }

fun <U, V> List<U>.groupSeparatedBy(
  separator: (U) -> Boolean,
  includeSeparator: Boolean = false,
  transform: (List<U>) -> V
): List<V> = sequence {
  var curr = mutableListOf<U>()
  forEach {
    if (separator(it) && curr.isNotEmpty()) yield(transform(curr))
    if (separator(it)) curr = if (includeSeparator) mutableListOf(it) else mutableListOf()
    else curr += it
  }
  if (curr.isNotEmpty()) yield(transform(curr))
}.toList()

fun <T> List<List<T>>.transpose(): List<List<T>> {
  val n = map { it.size }.toSet().singleOrNull()
    ?: throw IllegalArgumentException("Invalid data to transpose: $this")
  return List(n) { y -> List(size) { x -> this[x][y] } }
}

infix fun Int.directedTo(o: Int) = if (this <= o) this..o else this downTo o

class DefaultMap<K, V>(
  private val default: V,
  private val map: MutableMap<K, V> = HashMap()
) : MutableMap<K, V> by map {
  override fun get(key: K): V = map.getOrDefault(key, default).also { map[key] = it }
  operator fun plus(kv: Pair<K, V>): DefaultMap<K, V> = (map + kv).toDefaultMap(default)
  override fun toString() = map.toString()
  override fun hashCode() = map.hashCode()
  override fun equals(other: Any?) = map == other
}

fun <K, V> Map<K, V>.toDefaultMap(default: V) = DefaultMap(default, toMutableMap())

class LazyDefaultMap<K, V>(
  private val default: () -> V,
  private val map: MutableMap<K, V> = HashMap()
) : MutableMap<K, V> by map {
  override fun get(key: K): V = map.getOrDefault(key, default()).also { map[key] = it }
  operator fun plus(kv: Pair<K, V>): LazyDefaultMap<K, V> = (map + kv).toLazyDefaultMap(default)
  override fun toString() = map.toString()
  override fun hashCode() = map.hashCode()
  override fun equals(other: Any?) = map == other
}

fun <K, V> Map<K, V>.toLazyDefaultMap(default: () -> V) = LazyDefaultMap(default, toMutableMap())

fun catchSystemOut(action: () -> Unit) = ByteArrayOutputStream().also {
  val originalOut = System.out
  System.setOut(PrintStream(it))
  action()
  System.setOut(originalOut)
}.toString()
