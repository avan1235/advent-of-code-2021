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
