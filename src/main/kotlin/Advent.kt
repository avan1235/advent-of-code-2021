fun main() = AdventDay.all
  .forEach {
    println("--- ${it::class.java.simpleName}")
    it.solve()
  }
