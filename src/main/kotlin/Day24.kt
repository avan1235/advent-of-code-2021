object Day24 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return

    val instr = data.map { it.toInstr() }
    val reverseDigits = instr.reverseDigitsMappings()

    reverseDigits.findDigits(compareByDescending { it }).printIt()
    reverseDigits.findDigits(compareBy { it }).printIt()
  }
}

private fun String.toInstr(): Instr {
  val parts = split(" ")
  return when {
    parts.size == 2 && parts[0] == "inp" -> Inp(parts[1])
    parts.size == 3 -> BinOp(parts[1], Op.valueOf(parts[0].uppercase()), parts[2].toRight())
    else -> error("Unknown command type: $this")
  }
}

private fun String.toRight() = try {
  Num(toLong())
} catch (_: Exception) {
  Var(this)
}

private class ReverseDigits(private val mapping: List<LazyDefaultMap<Long, MutableSet<StartingWith>>>) {
  data class StartingWith(val z: Long, val digit: Long)

  fun findDigits(digitsComparator: Comparator<Long>): Long? {
    fun go(digitIdx: Int, forZ: Long, acc: List<Long>): List<Long>? =
      if (digitIdx < 0) acc.reversed()
      else reverseRegState(digitIdx, forZ)
        .sortedWith { l, r -> digitsComparator.compare(l.digit, r.digit) }
        .firstNotNullOfOrNull { go(digitIdx - 1, it.z, acc + it.digit) }

    val digits = go(digitIdx = mapping.size - 1, forZ = 0, acc = listOf())
    return digits?.joinToString("")?.toLongOrNull()
  }

  private fun reverseRegState(idx: Int, value: Long): Set<StartingWith> =
    if (idx in mapping.indices) mapping[idx][value] else emptySet()
}

private fun List<Instr>.reverseDigitsMappings(searchMax: Long = 1 shl 16): ReverseDigits =
  groupSeparatedBy(separator = { it is Inp }, includeSeparator = true) { instr ->
    LazyDefaultMap<Long, MutableSet<ReverseDigits.StartingWith>>(::mutableSetOf).also { finishedWith ->
      for (forDigit in 1L..9L) for (forZ in 0L..searchMax)
        ALU(forDigit, withState = mapOf("z" to forZ)).apply { run(instr) }
          .registers["z"].let { finishedWith[it].add(ReverseDigits.StartingWith(forZ, forDigit)) }
    }
  }.let { ReverseDigits(it) }

private typealias VarName = String
private typealias Left = VarName

private sealed interface Right
private data class Var(val name: VarName) : Right
private data class Num(val value: Long) : Right

private enum class Op(val action: (Long, Long) -> Long) {
  ADD(Long::plus), MUL(Long::times), DIV(Long::div), MOD(Long::mod), EQL({ l, r -> if (l == r) 1 else 0 })
}

private sealed interface Instr
private data class Inp(val toVar: VarName) : Instr
private data class BinOp(val left: Left, val op: Op, val right: Right) : Instr

private class ALU(vararg val input: Long, withState: Map<VarName, Long> = emptyMap()) {
  val registers = withState.toDefaultMap(0)
  private var inputIdx = 0

  fun run(instr: Iterable<Instr>) = instr.forEach { process(it) }

  private fun process(instr: Instr) = when (instr) {
    is Inp -> {
      registers[instr.toVar] = input[inputIdx]
      inputIdx += 1
    }
    is BinOp -> when (instr.right) {
      is Num -> instr.right.value
      is Var -> registers[instr.right.name]
    }.let { rVal -> registers[instr.left] = instr.op.action(registers[instr.left], rVal) }
  }
}
