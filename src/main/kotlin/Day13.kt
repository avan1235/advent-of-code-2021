object Day13 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return

    val paper = data.toPaper()
    val commands = data.toCommands()

    commands.firstOrNull()?.let { paper.fold(it) }?.dots?.size.printIt()
    commands.fold(paper) { p, cmd -> p.fold(cmd) }.printIt()
  }
}

private fun List<String>.toPaper() = takeWhile { it.isNotBlank() }.map { line ->
  line.split(",").map { it.value<Int>() }.let { (x, y) -> V2(x, y) }
}.let { Paper(it.toSet()) }

private fun String.toFoldCmd() = removePrefix("fold along ").split("=")
  .let { (axe, coord) -> FoldCmd(coord.toInt(), FoldAxe.valueOf(axe)) }

private fun List<String>.toCommands() = dropWhile { it.isNotBlank() }.drop(1).map { it.toFoldCmd() }

private data class V2(val x: Int, val y: Int)

private enum class FoldAxe { x, y }
private data class FoldCmd(val coord: Int, val axe: FoldAxe)

private data class Paper(val dots: Set<V2>) {

  fun fold(cmd: FoldCmd): Paper = with(cmd) {
    val (orig, mod) = when (axe) {
      FoldAxe.x -> dots.partition { it.x <= coord }.let { (left, right) ->
        Pair(left, right.map { it.copy(x = coord - (it.x - coord)) })
      }
      FoldAxe.y -> dots.partition { it.y <= coord }.let { (up, down) ->
        Pair(up, down.map { it.copy(y = coord - (it.y - coord)) })
      }
    }
    Paper((orig + mod).toSet())
  }

  override fun toString() = buildString {
    for (y in 0..dots.maxOf { it.y }) {
      for (x in 0..dots.maxOf { it.x }) {
        append(if (V2(x, y) in dots) '#' else '.')
      }
      appendLine()
    }
  }
}
