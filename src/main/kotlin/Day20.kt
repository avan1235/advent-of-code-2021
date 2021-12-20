object Day20 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val algorithm = data.toAlgorithm()
    val image = data.toImage()

    image.enhance(algorithm, times = 2).enlighten.size.printIt()
    image.enhance(algorithm, times = 50).enlighten.size.printIt()
  }
}

private val Char.isLight: Boolean get() = this == '#'

private fun List<String>.toAlgorithm() = take(1).single().let { Image.Algorithm(it) }

private fun List<String>.toImage() = drop(2).flatMapIndexed { y, line ->
  line.mapIndexedNotNull { x, c -> if (c.isLight) Pixel(x, y) else null }
}.toSet().let { Image(it, fillInfty = false) }

private data class Pixel(val x: Int, val y: Int) {
  infix fun on(s: Image.Surface) = x in s.x && y in s.y
}

private class Image(val enlighten: Set<Pixel>, val fillInfty: Boolean) {
  private val surface = with(enlighten) {
    Surface(minOf { it.x }..maxOf { it.x }, minOf { it.y }..maxOf { it.y })
  }

  fun enhance(algorithm: Algorithm, times: Int) = (1..times)
    .fold(this) { img, _ -> img.enhanceStep(algorithm) }

  private fun enhanceStep(algorithm: Algorithm): Image = buildSet {
    for (x in surface.x + 1) for (y in surface.y + 1) Pixel(x, y).let {
      val encoding = encoding(it)
      val state = algorithm(encoding)
      if (state) add(it)
    }
  }.let { Image(it, if (fillInfty) algorithm(0b111111111) else algorithm(0b000000000)) }

  private fun encoding(p: Pixel) = sequence {
    for (yi in -1..1) for (xi in -1..1) yield(Pixel(p.x + xi, p.y + yi))
  }
    .map { if (it on surface) it in enlighten else fillInfty }
    .fold(0) { acc, b -> 2 * acc + if (b) 1 else 0 }

  private operator fun IntRange.plus(i: Int) = first - i..last + i

  class Surface(val x: IntRange, val y: IntRange)

  class Algorithm(data: String) {
    private val lightOn: Set<Int> = data
      .mapIndexedNotNull { idx, c -> if (c.isLight) idx else null }.toSet()

    operator fun invoke(x: Int) = x in lightOn
  }
}
