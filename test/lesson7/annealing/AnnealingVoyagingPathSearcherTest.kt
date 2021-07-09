package lesson7.annealing

import lesson5.Graph
import lesson5.impl.GraphBuilder
import org.junit.jupiter.api.Tag
import kotlin.test.*
import java.util.*

class AnnealingVoyagingPathSearcherTest {

    // TODO: this test is very unstable. Do something about it
    @Ignore
    @Test
    @Tag("Example")
    fun findVoyagingPath() {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            addConnection(a, b, 10)
            addConnection(b, c, 15)
            addConnection(c, f, 30)
            addConnection(a, d, 20)
            addConnection(d, e, 25)
            addConnection(e, f, 15)
            addConnection(a, f, 40)
            addConnection(b, d, 10)
            addConnection(c, e, 5)
        }.build()
        val path = graph.findVoyagingPathAnnealing(startTemperature = 5000, iterationNumber = 2000)
        assertEquals(105, path.length)
        val vertices = path.vertices
        assertEquals(vertices.first(), vertices.last(), "Voyaging path $vertices must be loop!")
        val withoutLast = vertices.dropLast(1)
        val expected = listOf(graph["A"], graph["D"], graph["B"], graph["C"], graph["E"], graph["F"])
        assertEquals(expected.size, withoutLast.size, "Voyaging path $vertices must travel through all vertices!")
        expected.forEach {
            assertTrue(it in vertices, "Voyaging path $vertices must travel through all vertices!")
        }
    }

    @Ignore
    @Test
    @Tag("Example")
    // This test is too long to run in continuous build
    fun findRandomVoyagingPath() {
        val random = Random()
        val graph = GraphBuilder().apply {
            val vertices = mutableListOf<Graph.Vertex>()
            for (i in 0..99) {
                vertices += addVertex(i.toString())
            }
            for (i in 0..99) {
                for (j in i + 1..99) {
                    addConnection(vertices[i], vertices[j], 1 + random.nextInt(100))
                }
            }
        }.build()
        val path = graph.findVoyagingPathAnnealing(startTemperature = 3000, iterationNumber = 1000)
        println(path.length)
        println(path)
    }
}