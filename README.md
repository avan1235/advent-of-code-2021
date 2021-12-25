# 🎄🎁🎅 2021 Advent of Code in Kotlin 🎅🎁🎄

## Project goals

The project goal is to deliver some pretty, readable and concise solutions to Advent of Code 2021 problems all written
in Kotlin language. It should show the other developer how some constructions from the language can be used and how to
solve some kind of tricky problems that appear during the Advent of Code.

## Problems source

You can find all problems at the [page of Advent of Code 2021](https://adventofcode.com/2021). The description of each
problem contains some sample test data, but I also included my input data files from the contest in
the [resources'](./src/main/resources/input) directory of the project to make my project working with some sample, real
world data.

## Solution template

When soling each day problem I use my template of `AdventDay` that I defined
in [AdventDay.kt](./src/main/kotlin/AdventDay.kt) - it's worth looking into this definition by yourself, and also you
can read more about it [at my blog](https://kotlin-dev.ml/post/advent-of-code-2020-0/).

It's enough to create some Kotlin `object` that inherits from `AdventDay` to get the solution running. If you're
interested in the details, let's look into the definition of the [Advent.kt](./src/main/kotlin/Advent.kt)
to see how to find all `object` classes in Kotlin and run some method on them 😎.

To run all solutions, simply call `./gradlew run`

### Tests

The solutions are tested with the values gathered from [adventofcode.com](https://adventofcode.com/2021). Every day of
Advent has its expected output defined in [AdventTest.kt](./src/test/kotlin/AdventTest.kt) and in order to
verify correct state of the outputs for days we run every day solution and catch it standard output stream
to compare it with expected value.

To run all tests, simply call `./gradlew test`

## Problems

The problems solutions are included in project, but for every of them you can also find some corresponding article at my
website, where I discuss not only the given problem, but also some cool features of Kotlin or I deep dive into some
language constructions.

| Problem            | Solution | Blog Post       | Tags |
|--------------------|----------|-----------------|------|
| [Day 1: Sonar Sweep](https://adventofcode.com/2021/day/1) | [Day1.kt](./src/main/kotlin/Day1.kt)  | [Day 1 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-1/) | `Sequence<T>`, `windowed`     |
| [Day 2: Dive!](https://adventofcode.com/2021/day/2)       | [Day2.kt](./src/main/kotlin/Day2.kt)  | [Day 2 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-2/) | `fold`, `inline`, `takeIf`     |
| [Day 3: Binary Diagnostic](https://adventofcode.com/2021/day/3) | [Day3.kt](./src/main/kotlin/Day3.kt)  | [Day 3 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-3/) | readable, abstracted   |
| [Day 4: Giant Squid](https://adventofcode.com/2021/day/4) | [Day4.kt](./src/main/kotlin/Day4.kt)  | [Day 4 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-4/) | `groupDividedBy`, `transpose`, context    |
| [Day 5: Hydrothermal Venture](https://adventofcode.com/2021/day/5) | [Day5.kt](./src/main/kotlin/Day5.kt)  | [Day 5 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-5/) | `DefaultMap<K, V>`, `directedTo`, `IntProgression`     |
| [Day 6: Lanternfish](https://adventofcode.com/2021/day/6) | [Day6.kt](./src/main/kotlin/Day6.kt)  | [Day 6 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-6/) | `crossinline`, data structures   |
| [Day 7: The Treachery of Whales](https://adventofcode.com/2021/day/7) | [Day7.kt](./src/main/kotlin/Day7.kt)  | [Day 7 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-7/) | median, average, math, named args    |
| [Day 8: Seven Segment Search](https://adventofcode.com/2021/day/8) | [Day8.kt](./src/main/kotlin/Day8.kt)  | [Day 8 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-8/) | `operator fun`, deduction     |
| [Day 9: Smoke Basin](https://adventofcode.com/2021/day/9) | [Day9.kt](./src/main/kotlin/Day9.kt)  | [Day 9 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-9/) | `operator fun`, `tailrec fun`, dfs, bfs  |
| [Day 10: Syntax Scoring](https://adventofcode.com/2021/day/10) | [Day10.kt](./src/main/kotlin/Day10.kt)  | [Day 10 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-10/) | `foldRight`, `Stack<V>`, bracket pairing  |
| [Day 11: Dumbo Octopus](https://adventofcode.com/2021/day/11) | [Day11.kt](./src/main/kotlin/Day11.kt)  | [Day 11 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-11/) | `Sequence<T>`, `operator fun set`, `operator fun get`, `LazyDefaultMap<K, V>`  |
| [Day 12: Passage Pathing](https://adventofcode.com/2021/day/12) | [Day12.kt](./src/main/kotlin/Day12.kt)  | [Day 12 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-12/) | `@JvmInline`, `value class`, dfs, paths in graph  |
| [Day 13: Transparent Origami](https://adventofcode.com/2021/day/13) | [Day13.kt](./src/main/kotlin/Day13.kt)  | [Day 13 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-13/) | `fold`, immutable data, reflections, origami  |
| [Day 14: Extended Polymerization](https://adventofcode.com/2021/day/14) | [Day14.kt](./src/main/kotlin/Day14.kt)  | [Day 14 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-14/) | immutable data, data structure, `buildMap { }`, `operator fun invoke`  |
| [Day 15: Chiton](https://adventofcode.com/2021/day/15) | [Day15.kt](./src/main/kotlin/Day15.kt)  | [Day 15 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-15/) | weighted graph, dijkstra, shortest path  |
| [Day 16: Packet Decoder](https://adventofcode.com/2021/day/16) | [Day16.kt](./src/main/kotlin/Day16.kt)  | [Day 16 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-16/) | `by notNull()`, delegated properties, `Sequence<T>`, date parsing  |
| [Day 17: Trick Shot](https://adventofcode.com/2021/day/17) | [Day17.kt](./src/main/kotlin/Day17.kt)  | [Day 17 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-17/) | simulation, physics, immutable data  |
| [Day 18: Snailfish](https://adventofcode.com/2021/day/18) | [Day18.kt](./src/main/kotlin/Day18.kt)  | [Day 18 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-18/) | tree, `reduce`, traversal, `lateinit var` |
| [Day 19: Beacon Scanner](https://adventofcode.com/2021/day/19) | [Day19.kt](./src/main/kotlin/Day19.kt)  | [Day 19 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-19/) | 3D rotation, matching, `infix fun`, named block |
| [Day 20: Trench Map](https://adventofcode.com/2021/day/20) | [Day20.kt](./src/main/kotlin/Day20.kt)  | [Day 20 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-20/) | `infix fun`, `operator fun`, extension properties, `IntRange` |
| [Day 21: Dirac Dice](https://adventofcode.com/2021/day/21) | [Day21.kt](./src/main/kotlin/Day21.kt)  | [Day 21 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-21/) | simulate, immutable data, `DefaultMap<K, V>`, counting, `fold`, `generateSequence` |
| [Day 22: Reactor Reboot](https://adventofcode.com/2021/day/22) | [Day22.kt](./src/main/kotlin/Day22.kt)  | [Day 22 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-22/) | `IntRange`, `Range3D`, `infix fun`, `operator fun` |
| [Day 23: Amphipod](https://adventofcode.com/2021/day/23) | [Day23.kt](./src/main/kotlin/Day23.kt)  | [Day 23 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-23/) | `HashSet`, simulation, memorization, Dijkstra, `data class` |
| [Day 24: Arithmetic Logic Unit](https://adventofcode.com/2021/day/24) | [Day24.kt](./src/main/kotlin/Day24.kt)  | [Day 24 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-24/) | ALU, reverse engineering, interpreter, `sealed interface` vs `enum class` |
| [Day 25: Sea Cucumber](https://adventofcode.com/2021/day/25) | [Day25.kt](./src/main/kotlin/Day25.kt)  | [Day 25 Blog Post](https://kotlin-dev.ml/post/advent-of-code-2021-25/) | immutable `data class`, function receiver, `Sequence<T>` |
