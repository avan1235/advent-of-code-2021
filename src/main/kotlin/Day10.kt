import java.util.*

object Day10 : AdventDay() {
  override fun solve() {
    val lines = reads<String>() ?: return

    lines.sumOf { it.corruptedScore() }.printIt()
    lines.mapNotNull { it.completionScore() }.sorted().let { it[it.size / 2] }.printIt()
  }
}

private val OPEN = setOf('[', '{', '(', '<')
private val CLOSE = setOf(']', '}', ')', '>')

private val Char.closed: Char?
  get() = when (this) {
    '{' -> '}'
    '(' -> ')'
    '[' -> ']'
    '<' -> '>'
    else -> null
  }

private fun String.corruptedScore(): Int {
  val stack = Stack<Char>()
  val firstCorrupted = firstOrNull { c ->
    when (c) {
      in OPEN -> stack.push(c).let { false }
      in CLOSE -> stack.pop().closed != c
      else -> unknownBracket(c)
    }
  }
  return when (firstCorrupted) {
    ')' -> 3
    ']' -> 57
    '}' -> 1197
    '>' -> 25137
    else -> 0
  }
}

private fun String.completionScore(): Long? {
  val stack = Stack<Char>()
  for (c in this) {
    when (c) {
      in OPEN -> stack.push(c)
      in CLOSE -> if (stack.pop().closed != c) return null
      else -> unknownBracket(c)
    }
  }
  return stack.foldRight(0L) { c, sum ->
    5 * sum + when (val b = c.closed) {
      ')' -> 1
      ']' -> 2
      '}' -> 3
      '>' -> 4
      else -> unknownBracket(c)
    }
  }
}

private fun unknownBracket(c: Char): Nothing =
  throw IllegalArgumentException("Unknown bracket: $c")
