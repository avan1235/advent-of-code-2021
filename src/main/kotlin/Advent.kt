fun main() = AdventDay::class.sealedSubclasses
  .mapNotNull { it.objectInstance }
  .sortedBy { it::class.java.simpleName.removePrefix("Day").toInt() }
  .forEach {
    println("--- ${it::class.java.simpleName}")
    it.solve()
  }
