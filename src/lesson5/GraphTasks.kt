@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.impl.GraphBuilder
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    /**
     * time complexity : O(E + V)
     * auxiliary space : O(E + V)
     * E - edges, V - vertices
     */

    //Count of incident edges should be even
    //because you need go to this vertex and out from it
    //equal counts of a time
    for (vertex in vertices) {
        if (getNeighbors(vertex).size % 2 == 1)
            return emptyList()
    }
    val stack = Stack<Graph.Vertex>()
    val solution = ArrayDeque<Graph.Vertex>()
    stack.push(vertices.first())
    val edgesList = mutableSetOf<Graph.Edge>()
    edgesList.addAll(edges)
    while (!stack.isEmpty()) {
        val sVertex = stack.lastElement()
        for (vertex in vertices) {
            val edge = getConnection(sVertex, vertex)
            if (edge != null && edgesList.contains(edge)) {
                stack.push(vertex)
                edgesList.remove(edge)
                break
            }
        }
        if (sVertex == stack.last()) {
            stack.pop()
            solution.add(sVertex)
        }
    }
    val eulerCycle = mutableListOf<Graph.Edge>()
    for (count in 0 until solution.size - 1){
        eulerCycle.add(getConnection(solution.poll(), solution.first)!!)
    }
    return eulerCycle
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    /**
     * time complexity : O(E + V)
     * auxiliary space : O(E + V)
     * E - edges, V - vertices
     */
    val start = this.vertices.first()
    val info = mutableMapOf<Graph.Vertex, VertexInfo>()
    for (vertex in this.vertices) {
        info[vertex] = VertexInfo(vertex, Int.MAX_VALUE, null)
    }
    val startInfo = VertexInfo(start, 0, null)
    val queue = PriorityQueue<VertexInfo>()
    queue.add(startInfo)
    info[start] = startInfo
    while (queue.isNotEmpty()) {
        val currentInfo = queue.poll()
        val currentVertex = currentInfo.vertex
        for (vertex in this.getNeighbors(currentVertex)) {
            val weight = this.getConnection(currentVertex, vertex)?.weight
            if (weight != null) {
                val newDistance = info[currentVertex]!!.distance + weight
                if (info[vertex]!!.distance > newDistance) {
                    val newInfo = VertexInfo(vertex, newDistance, currentVertex)
                    queue.add(newInfo)
                    info[vertex] = newInfo
                }
            }
        }
    }
    var index = 1
    return GraphBuilder().apply {
        info.map {
            if (index > 1) {
                addVertex("${it.value.vertex}")
                addConnection(it.value.prev!!, it.key)
            }
            index++
        }
    }.build()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    /**
     * time complexity : ???
     * auxiliary space : ???
     */
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    /**
     * time complexity : ???
     * auxiliary space : ???
     */
    TODO()
}
