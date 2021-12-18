object Day18 : AdventDay() {
  override fun solve() {
    val data = reads<String>() ?: return
    val snailFish = data.map { it.toSnailFish() }

    snailFish.reduce { l, r -> l + r }.magnitude().printIt()
    sequence {
      for (l in snailFish) for (r in snailFish)
        if (l != r) yield((l + r).magnitude())
    }.maxOf { it }.printIt()
  }
}

private sealed interface TreeNode {
  var parent: TreeParent?
  fun copy(with: TreeParent? = null): TreeNode
}

private class TreeLeaf(var value: Int, override var parent: TreeParent?) : TreeNode {
  override fun copy(with: TreeParent?) = TreeLeaf(value, with)
}

private class TreeParent(override var parent: TreeParent?) : TreeNode {
  lateinit var left: TreeNode
  lateinit var right: TreeNode
  override fun copy(with: TreeParent?) = TreeParent(with).also {
    it.left = left.copy(it)
    it.right = right.copy(it)
  }
}

private fun String.toSnailFish(): TreeNode = asSequence().run {
  fun Sequence<Char>.parse(parent: TreeParent? = null): Pair<TreeNode, Sequence<Char>> = when (first()) {
    '[' -> {
      val fish = TreeParent(parent)
      val (left, fstRest) = drop("[".length).parse(fish)
      val (right, sndRest) = fstRest.drop(",".length).parse(fish)
      Pair(fish.also { it.left = left; it.right = right }, sndRest.drop("]".length))
    }
    else -> Pair(TreeLeaf(first().digitToInt(), parent), dropWhile { it.isDigit() })
  }
  parse().let { (snailFish, _) -> snailFish }
}

private fun TreeNode.magnitude(): Long = when (this) {
  is TreeLeaf -> value.toLong()
  is TreeParent -> 3 * left.magnitude() + 2 * right.magnitude()
}

private operator fun TreeNode.plus(other: TreeNode) = TreeParent(parent = null).also { parent ->
  parent.left = this.copy(parent)
  parent.right = other.copy(parent)
}.apply { reduce() }

private tailrec fun TreeNode.updateOnMost(select: TreeParent.() -> TreeNode, update: (Int) -> Int): Unit = when (this) {
  is TreeLeaf -> value = update(value)
  is TreeParent -> select().updateOnMost(select, update)
}

private tailrec fun TreeParent.goUpFrom(select: TreeParent.() -> TreeNode): TreeParent? {
  val currParent = parent
  return if (currParent == null) currParent
  else if (currParent.select() != this) currParent.goUpFrom(select)
  else currParent
}

private fun TreeNode.leftFinalParent(): TreeParent? = when {
  this is TreeParent && left is TreeLeaf && right is TreeLeaf -> this
  this is TreeParent -> left.leftFinalParent() ?: right.leftFinalParent()
  else -> null
}

private fun TreeNode.changeTo(createNode: (TreeParent?) -> TreeNode) = when {
  parent?.right == this -> parent?.right = createNode(parent)
  parent?.left == this -> parent?.left = createNode(parent)
  else -> Unit
}

private fun TreeLeaf.split() = changeTo { parent ->
  TreeParent(parent).apply {
    left = TreeLeaf(value / 2, this)
    right = TreeLeaf(value / 2 + value % 2, this)
  }
}

private val TreeParent.leftValue: Int get() = (left as? TreeLeaf)?.value ?: 0
private val TreeParent.rightValue: Int get() = (right as? TreeLeaf)?.value ?: 0

private fun TreeNode.reduce() {
  fun TreeNode.findToExplode(level: Int): TreeParent? = when {
    level == 0 -> leftFinalParent()
    level > 0 && this is TreeParent -> left.findToExplode(level - 1) ?: right.findToExplode(level - 1)
    else -> null
  }

  fun TreeNode.findToSplit(): TreeLeaf? = when (this) {
    is TreeLeaf -> if (value > 9) this else null
    is TreeParent -> left.findToSplit() ?: right.findToSplit()
  }

  while (true) {
    val explode = findToExplode(level = 4)
    if (explode == null) findToSplit()?.split() ?: break
    else {
      explode.goUpFrom { right }?.left?.updateOnMost({ right }) { it + explode.leftValue }
      explode.goUpFrom { left }?.right?.updateOnMost({ left }) { it + explode.rightValue }
      explode.changeTo { parent -> TreeLeaf(0, parent) }
    }
  }
}
