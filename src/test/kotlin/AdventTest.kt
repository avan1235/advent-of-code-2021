import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AdventTest {

  @Test
  fun `test days outputs`() {
    expectedOutputs.forEachIndexed { idx, expect ->
      val out = catchSystemOut { AdventDay.all[idx].solve() }
      assertEquals(expect, out)
    }
  }

  private val expectedOutputs = mutableListOf(
    "1553\n1597\n",
    "1989265\n2089174012\n",
    "3958484\n1613181\n",
    "82440\n20774\n",
    "6311\n19929\n",
    "374994\n1686252324092\n",
    "352331\n99266250\n",
    "369\n1031553\n",
    "439\n900900\n",
    "318081\n4361305341\n",
    "1620\n371\n",
    "4773\n116985\n",
    "706\n" +
      "#....###..####...##.###....##.####.#..#\n" +
      "#....#..#.#.......#.#..#....#.#....#..#\n" +
      "#....#..#.###.....#.###.....#.###..####\n" +
      "#....###..#.......#.#..#....#.#....#..#\n" +
      "#....#.#..#....#..#.#..#.#..#.#....#..#\n" +
      "####.#..#.#.....##..###...##..####.#..#\n\n",
    "2360\n2967977072188\n",
    "720\n3025\n",
  )
}
