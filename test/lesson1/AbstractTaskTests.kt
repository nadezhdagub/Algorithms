package lesson1

import org.junit.jupiter.api.assertThrows
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.math.abs
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

abstract class AbstractTaskTests : AbstractFileTests() {

    protected fun sortTimes(sortTimes: (String, String) -> Unit) {
        try {
            sortTimes("input/time_in1.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                     00:40:31
                     07:26:57
                     10:00:03
                     13:15:19
                     13:15:19
                     19:56:14
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortTimes("input/time_in2.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                     00:00:00
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortTimes("input/time_in3.txt", "temp.txt")
            assertFileContent("temp.txt", File("input/time_out3.txt").readLines().joinToString(separator = "\n"))
        } finally {
            File("temp.txt").delete()
        }

        //Mine tests
        try {
            assertThrows<FileNotFoundException> { sortTimes("FileNotFound", "temp.txt") }
        } finally {
            File("temp.txt").delete()
        }
        try {
            assertThrows<NumberFormatException> { sortTimes("input/time_in4.txt", "temp.txt") }
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortTimes("input/empty.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
    }

    protected fun sortAddresses(sortAddresses: (String, String) -> Unit) {
        // TODO: large test
        try {
            sortAddresses("input/addr_in1.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                    Железнодорожная 3 - Петров Иван
                    Железнодорожная 7 - Иванов Алексей, Иванов Михаил
                    Садовая 5 - Сидоров Петр, Сидорова Мария
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortAddresses("input/empty.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
    }

    private fun generateTemperatures(size: Int) {
        val random = Random()
        val temperatures = mutableListOf<Int>()
        for (t in -2730..5000) {
            val count = random.nextInt(size)
            for (i in 1..count) {
                temperatures += t
            }
        }

        fun BufferedWriter.writeTemperatures() {
            for (t in temperatures) {
                if (t < 0) write("-")
                write("${abs(t) / 10}.${abs(t) % 10}")
                newLine()
            }
            close()
        }

        File("temp_sorted_expected.txt").bufferedWriter().writeTemperatures()
        temperatures.shuffle(random)
        File("temp_unsorted.txt").bufferedWriter().writeTemperatures()
    }

    protected fun sortTemperatures(sortTemperatures: (String, String) -> Unit) {
        try {
            sortTemperatures("input/temp_in1.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                    -98.4
                    -12.6
                    -12.6
                    11.0
                    24.7
                    99.5
                    121.3
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
        try {
            assertThrows<Throwable>("Not in range") { sortTemperatures("input/temp_in2.txt", "temp.txt") }
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortTemperatures("input/empty.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
        fun testGeneratedTemperatures(size: Int) {
            try {
                generateTemperatures(size)
                sortTemperatures("temp_unsorted.txt", "temp_sorted_actual.txt")
                assertFileContent("temp_sorted_actual.txt",
                        File("temp_sorted_expected.txt").readLines().joinToString(separator = "\n")
                )
            } finally {
                File("temp_unsorted.txt").delete()
                File("temp_sorted_expected.txt").delete()
                File("temp_sorted_actual.txt").delete()
            }
        }
        testGeneratedTemperatures(10)
        testGeneratedTemperatures(500)
    }

    protected fun sortSequence(sortSequence: (String, String) -> Unit) {
        // TODO: large test
        try {
            sortSequence("input/seq_in1.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                        1
                        3
                        3
                        1
                        2
                        2
                        2
                    """.trimIndent())
        } finally {
            File("temp.txt").delete()
        }
        try {
            sortSequence("input/seq_in2.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                        25
                        39
                        25
                        39
                        25
                        39
                        12
                        12
                        12
                    """.trimIndent())
        } finally {
            File("temp.txt").delete()
        }
        try {
            assertThrows<java.lang.NumberFormatException> { sortSequence("input/seq_in3.txt", "temp.txt") }
        } finally {
            File("temp.txt").delete()
        }
        try {
            assertThrows<kotlin.KotlinNullPointerException> { sortSequence("input/empty.txt", "temp.txt") }
        } finally {
            File("temp.txt").delete()
        }
        fun BufferedWriter.writeNumbers(numbers: List<Int>) {
            for (n in numbers) {
                write("$n")
                newLine()
            }
            close()
        }

        fun generateSequence(totalSize: Int, answerSize: Int) {
            val random = Random()
            val numbers = mutableListOf<Int>()

            val answer = 100000 + random.nextInt(100000)
            val count = mutableMapOf<Int, Int>()
            for (i in 1..totalSize - answerSize) {
                var next: Int
                var nextCount: Int
                do {
                    next = random.nextInt(answer - 1) + 1
                    nextCount = count[next] ?: 0
                } while (nextCount >= answerSize - 1)
                numbers += next
                count[next] = nextCount + 1
            }
            for (i in totalSize - answerSize + 1..totalSize) {
                numbers += answer
            }
            File("temp_sequence_expected.txt").bufferedWriter().writeNumbers(numbers)
            for (i in totalSize - answerSize until totalSize) {
                numbers.removeAt(totalSize - answerSize)
            }
            for (i in totalSize - answerSize until totalSize) {
                val toInsert = random.nextInt(totalSize - answerSize)
                numbers.add(toInsert, answer)

            }
            File("temp_sequence.txt").bufferedWriter().writeNumbers(numbers)
        }

        try {
            generateSequence(500000, 200)
            sortSequence("temp_sequence.txt", "temp.txt")
            assertFileContent("temp.txt", File("temp_sequence_expected.txt").readLines().joinToString("\n"))
        } finally {
            File("temp_sequence_expected.txt").delete()
            File("temp_sequence.txt").delete()
            File("temp.txt").delete()
        }
    }

    protected fun generateArrays(firstSize: Int, secondSize: Int): Triple<Array<Int>, Array<Int?>, Array<Int?>> {
        val random = Random()
        val expectedResult = Array<Int?>(firstSize + secondSize) {
            it * 10 + random.nextInt(10)
        }
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int?>()
        for (i in 1..firstSize) second.add(null)
        for (element in expectedResult) {
            if (first.size < firstSize && (random.nextBoolean() || second.size == firstSize + secondSize)) {
                first += element!!
            } else {
                second += element
            }
        }
        return Triple(first.toTypedArray(), second.toTypedArray(), expectedResult)
    }
}