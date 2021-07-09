package lesson5

import lesson5.impl.GraphBuilder
import org.junit.jupiter.api.Tag
import kotlin.test.*

class DijkstraTest {

    @Test
    @Tag("Example")
    fun test1() {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b, 10)
            addConnection(b, c, 5)
        }.build()
        val pathMap = graph.shortestPath(graph["A"]!!)
        assertEquals(10, pathMap[graph["B"]!!]?.distance)
        assertEquals(15, pathMap[graph["C"]!!]?.distance)
        assertEquals(listOf(graph["A"], graph["B"], graph["C"]), pathMap.unrollPath(graph["C"]!!))
    }
}