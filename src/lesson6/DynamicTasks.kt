@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    TODO()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Средняя
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    /**
     * time complexity O(n * n/2)
     * space complexity O(n)
     */
    if (list.isEmpty()) return emptyList()
    val arr = list.toIntArray()
    val lengthArray = IntArray(list.size) { 1 }
    val indexArray = IntArray(list.size)

    for (index in 0 until arr.size){
        lengthArray[index] = 1
        indexArray[index] = index
    }

    for (index1 in 1 until arr.size){
        for (index2 in 0 until index1){
            if (arr[index1] > arr[index2]){
                if (lengthArray[index2]+1 > lengthArray[index1]){
                    lengthArray[index1] = lengthArray[index2] + 1
                    indexArray[index1] = index2
                }
            }
        }
    }
    var maxIndex = 0
    for (index in 0 until lengthArray.size) {
        if (lengthArray[index] > lengthArray[maxIndex]) {
            maxIndex = index
        }
    }
    var index: Int
    val longestSequence = mutableListOf<Int>()
    var newIndex = maxIndex
    do {
        index = newIndex
        longestSequence.add(arr[index])
        newIndex = indexArray[index]
    } while (index != newIndex)

    return longestSequence.reversed()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Сложная
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    /**
     * time complexity = O(m * n)
     * space complexity = O(m * n)
     */
    val taskMatrix = File(inputName).readLines()
            .map { it -> it.split(' ').map { it.toInt() }.toIntArray() }
            .toTypedArray()
    val m = taskMatrix.size - 1
    val n = taskMatrix[0].size - 1
    val result = Array(m + 1) { IntArray(n + 1) { 0 } }
    var sum = 0

    for (index in 0..n){
        sum += taskMatrix[0][index]
        result[0][index] = sum
    }
    sum = 0
    for (index in 0..m){
        sum += taskMatrix[index][0]
        result[index][0] = sum
    }
    for (index1 in 1..m)
        for (index2 in 1..n)
            result[index1][index2] =
                    taskMatrix[index1][index2] +
                    minOf(result[index1 - 1][index2 - 1], result[index1 - 1][index2], result[index1][index2 - 1])
    return result[m][n]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5
