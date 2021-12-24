fun main() = AdventDay.all
  .forEach {
    println("--- ${it::class.java.simpleName}")
    if (it is Day23) it.solve()
  }
