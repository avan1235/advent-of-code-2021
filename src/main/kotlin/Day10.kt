import java.util.*

object Day10 : AdventDay() {
  override fun solve() {
    val lines = reads<String>() ?: return

    lines.sumOf { it.syntaxErrorScore() }.printIt()
    lines.mapNotNull { it.score() }.sorted().let { it[it.size / 2] }.printIt()
  }
}

private val OPEN = setOf('[', '{', '(', '<')
private val CLOSE = setOf(']', '}', ')', '>')

private val Char.rev: Char
  get() = when (this) {
    '{' -> '}'
    '(' -> ')'
    '[' -> ']'
    '<' -> '>'
    else -> unknownBracket(this)
  }

private fun String.syntaxErrorScore(): Int {
  val s = Stack<Char>()
  val c = firstOrNull {
    when (it) {
      in OPEN -> s.push(it)
      in CLOSE -> if (s.pop().rev != it) return@firstOrNull true
      else -> unknownBracket(it)
    }
    false
  }
  return when (c) {
    ')' -> 3
    ']' -> 57
    '}' -> 1197
    '>' -> 25137
    else -> 0
  }
}

private fun String.score(): Long? {
  val s = Stack<Char>()
  for (c in this) {
    when (c) {
      in OPEN -> s.push(c)
      in CLOSE -> if (s.pop().rev != c) return null
      else -> unknownBracket(c)
    }
  }
  return s.foldRight(0L) { c, sum ->
    5 * sum + when (val b = c.rev) {
      ')' -> 1L
      ']' -> 2L
      '}' -> 3L
      '>' -> 4L
      else -> unknownBracket(b)
    }
  }
}

private fun unknownBracket(c: Char): Nothing =
  throw IllegalArgumentException("Unknown bracket: $c")
