import kotlin.properties.Delegates.notNull

object Day16 : AdventDay() {
  override fun solve() {
    val data = reads<String>()?.singleOrNull() ?: return
    val bits = data.asSequence().flatMap { it.toBits() }

    val (packet, rest) = buildPacket(from = bits)
    rest.requireZeros()
    packet.sumVersionNumbers().printIt()
    packet.eval().printIt()
  }
}

private typealias Bits = Sequence<Int>

private fun Packet.sumVersionNumbers(): Int = when (this) {
  is NumberPacket -> header.version
  is OpPacket -> header.version + subPackets.sumOf { it.sumVersionNumbers() }
}

private fun Packet.eval(): Long = when (this) {
  is NumberPacket -> value
  is OpPacket -> when (header.type) {
    0 -> subPackets.fold(0L) { acc, p -> acc + p.eval() }
    1 -> subPackets.fold(1L) { acc, p -> acc * p.eval() }
    2 -> subPackets.minOf { it.eval() }
    3 -> subPackets.maxOf { it.eval() }
    5 -> subPackets.let { (l, r) -> if (l.eval() > r.eval()) 1 else 0 }
    6 -> subPackets.let { (l, r) -> if (l.eval() < r.eval()) 1 else 0 }
    7 -> subPackets.let { (l, r) -> if (l.eval() == r.eval()) 1 else 0 }
    else -> throw IllegalStateException("Unknown combination of data in packet: $this")
  }
}

private fun buildPacket(from: Bits): Pair<Packet, Bits> = PacketHeader().run {
  val bits = from
    .use(3) { version = it.msb().toInt() }
    .use(3) { type = it.msb().toInt() }

  when (type) {
    4 -> buildNumberPacket(from = bits)
    else -> buildOpPacket(from = bits)
  }
}

private fun PacketHeader.buildNumberPacket(from: Bits) = NumberPacket(header = this).run {
  var bits = from
  var reading = true
  while (reading) {
    bits = bits.use(1) { reading = it.first() == 1 }
    bits = bits.use(4) { value = value * 16 + it.msb() }
  }
  Pair(this, bits)
}

private fun PacketHeader.buildOpPacket(from: Bits) = OpPacket(header = this).run {
  var bits = from
    .use(1) { countSubPackets = it.first() == 1 }
    .use(if (countSubPackets) 11 else 15) { subPacketsCounter = it.msb().toInt() }
  subPackets = if (countSubPackets)
    buildList {
      repeat(subPacketsCounter) {
        val (subPacket, subBits) = buildPacket(from = bits)
        add(subPacket).also { bits = subBits }
      }
    }
  else buildList {
    bits = bits.use(subPacketsCounter) {
      bits = it
      while (bits.any()) {
        val (subPacket, subBits) = buildPacket(from = bits)
        add(subPacket).also { bits = subBits }
      }
    }
  }
  Pair(this, bits)
}

private fun Char.toBits() = "$this".toInt(radix = 16).let {
  sequenceOf((it / 8) % 2, (it / 4) % 2, (it / 2) % 2, it % 2)
}

private inline fun <T> Sequence<T>.use(n: Int, action: (Sequence<T>) -> Unit): Sequence<T> {
  sequence { yieldAll(take(n)) }.let(action)
  return drop(n)
}

private fun Bits.msb() = fold(0L) { acc, b -> 2 * acc + b }

private class PacketHeader {
  var version: Int by notNull()
  var type: Int by notNull()
}

private sealed class Packet(val header: PacketHeader)
private class NumberPacket(header: PacketHeader) : Packet(header) {
  var value: Long = 0
}

private class OpPacket(header: PacketHeader) : Packet(header) {
  var countSubPackets: Boolean by notNull()
  var subPacketsCounter: Int by notNull()
  var subPackets: List<Packet> by notNull()
}

private fun Bits.requireZeros() =
  if (any { it == 1 }) throw IllegalStateException("Left non zero bytes") else Unit
