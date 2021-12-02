object Day2 : AdventDay() {
    override fun solve() {
        val commands = reads<String>()?.map { it.cmd() } ?: return
        commands.calcPosition().run { first * second }.printIt()
        commands.calcAimedPosition().run { first * second }.printIt()
    }

    private fun List<Cmd>.calcPosition() = fold(Pair(0, 0)) { (x, y), (dir, v) ->
        when (dir) {
            Forward -> Pair(x + v, y)
            Down -> Pair(x, y + v)
            Up -> Pair(x, y - v)
        }
    }

    private fun List<Cmd>.calcAimedPosition() = fold(Triple(0, 0, 0)) { (x, y, a), (dir, v) ->
        when (dir) {
            Down -> Triple(x, y, a + v)
            Up -> Triple(x, y, a - v)
            Forward -> Triple(x + v, y + a * v, a)
        }
    }
}

private data class Cmd(val dir: Dir, val v: Int)
private sealed interface Dir
object Forward : Dir
object Up : Dir
object Down : Dir

private fun String.cmd() = split(" ").takeIf { it.size == 2 }?.let { (dir, v) ->
    when (dir) {
        "forward" -> Cmd(Forward, v.toInt())
        "up" -> Cmd(Up, v.toInt())
        "down" -> Cmd(Down, v.toInt())
        else -> throw IllegalArgumentException("Unknown direction specified in data: $dir")
    }
} ?: throw IllegalArgumentException("Invalid data format")
