package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        /**
         * time complexity : O(logN)
         * auxiliary space : O(1)
         */
        if (find(element) == null) return false
        var parent = root ?: return false
        var curr = root ?: return false
        //find current point via element and parent
        while (curr.value != element) {
            parent = curr
            curr = if (curr.value > element)
                curr.left ?: return false
            else
                curr.right ?: return false
        }
        if (curr.left == null && curr.right == null) {
            //null both of children
			setNode(curr, parent, null)
        } else if (curr.left == null) {
            //null left child
            setNode(curr, parent, curr.right)
        } else if (curr.right == null) {
            //null right child
            setNode(curr, parent, curr.left)
        } else {
            //not null both of children
            var minNode = curr.right ?: return false
            var parentMinNode = curr.right ?: return false
            while (minNode.left != null) {
                parentMinNode = minNode
                val left = minNode.left ?: return false
                minNode = left
            }
            when {
                curr == root && parentMinNode == minNode -> {
                    val rootLeft = root!!.left
                    root = minNode
                    minNode.left = rootLeft
                }
                curr == root && parentMinNode != minNode -> {
                    parentMinNode.left = minNode.right
                    root = minNode
                    minNode.left = curr.left
                    minNode.right = curr.right
                }
                parentMinNode == minNode -> setNode(curr, parent, minNode)
                else -> {
                    parentMinNode.left = minNode.right
                    minNode.right = curr.right
                    minNode.left = curr.left
                    setNode(curr, parent, minNode)
                }
            }
            minNode.left = curr.left
        }
        size--
        return true
    }

    private fun setNode(curr: Node<T>, parent: Node<T>, set: Node<T>?) {
        when (curr) {
            root -> root = set
            parent.left -> parent.left = set
            parent.right -> parent.right = set
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private fun findNext(): Node<T>? {
            /**
             * time complexity : O(logN)
             * auxiliary space : O(1)
             */
            if (size == 0) return null
            val currentNode = current ?: return find(first())
            if (currentNode.value == last()) return null
            if (currentNode.right != null) {
                var successor = currentNode.right ?: throw IllegalArgumentException()
                while (successor.left != null) {
                    successor = successor.left ?: return successor
                }
                return successor
            } else {
                var successor = root ?: throw IllegalArgumentException()
                var ancestor = root ?: throw IllegalArgumentException()
                while (ancestor != currentNode) {
                    if (currentNode.value < ancestor.value) {
                        successor = ancestor
                        ancestor = ancestor.left ?: throw IllegalArgumentException()
                    } else ancestor = ancestor.right ?: throw IllegalArgumentException()
                }
                return successor
            }
        }


        override fun hasNext(): Boolean = findNext() != null

        override fun next(): T {
            current = findNext()
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            /**
             * time complexity : O(logN)
             * auxiliary space : O(1)
             */
            val cur = current ?: return
            current = findNext()
            remove(cur.value)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        /**
         * time complexity : ???
         * auxiliary space : ???
         */
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        /**
         * time complexity : ???
         * auxiliary space : ???
         */
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        /**
         * time complexity : ???
         * auxiliary space : ???
         */
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
