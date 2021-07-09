@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
 * каждый на отдельной строке. Пример:
 *
 * 13:15:19
 * 07:26:57
 * 10:00:03
 * 19:56:14
 * 13:15:19
 * 00:40:31
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 00:40:31
 * 07:26:57
 * 10:00:03
 * 13:15:19
 * 13:15:19
 * 19:56:14
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    /**
     * time complexity : O(N^2)
     * auxiliary space : O(N)
     */
    val list = File(inputName).readLines()
            .asSequence()
            .map { it -> it.split(":") }
            .map { it ->
                val (h, m, s) = it
                h.toInt() * 3600 + m.toInt() * 60 + s.toInt()
            }
            .toList().toIntArray()
    insertionSort(list)
    File(outputName).writer().run {
        list.forEach { it ->
            write("%02d:%02d:%02d\n".format(it / 3600, (it / 60) % 60, it % 60))
        }
        close()
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    /**
     * time complexity : O(N)
     * auxiliary space : O(n*m) n = adrMap.keys.size,  m = adrMap.values.size
     */
    val adrMap: TreeMap<String, ArrayList<String>> = TreeMap()
    File(inputName).readLines().forEach { it ->
        var (name, adr) = it.split("-")
        name = name.trim()
        adr = adr.trim()
        if (adrMap.containsKey(adr))
            adrMap[adr]?.add(name)
        else
            adrMap[adr] = arrayListOf(name)
    }
    File(outputName).writer().run {
        adrMap.forEach { it ->
            write("${it.key} - ${it.value.toString().substring(1, it.value.toString().length - 1)}\n")
        }
        close()
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    /**
     * time complexity : O(N*logN)
     * auxiliary space : O(N)
     */
    val list = File(inputName).readLines().map { it.toDouble() }.toDoubleArray()
    mergeDoubleSort(list, 0, list.size)
    File(outputName).writer().run {
        list.forEach { it ->
            if (it !in -273.0..500.0) throw Throwable("Not in range")
            write("$it\n")
        }
        close()
    }
}

//merge sort for DoubleArray
private fun merge(elements: DoubleArray, begin: Int, middle: Int, end: Int) {
    val left = Arrays.copyOfRange(elements, begin, middle)
    val right = Arrays.copyOfRange(elements, middle, end)
    var li = 0
    var ri = 0
    for (i in begin until end) {
        if (li < left.size && (ri == right.size || left[li] <= right[ri])) {
            elements[i] = left[li++]
        } else {
            elements[i] = right[ri++]
        }
    }
}

private fun mergeDoubleSort(elements: DoubleArray, begin: Int, end: Int) {
    if (end - begin <= 1) return
    val middle = (begin + end) / 2
    mergeDoubleSort(elements, begin, middle)
    mergeDoubleSort(elements, middle, end)
    merge(elements, begin, middle, end)
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    /**
     * time complexity : O(N)
     * auxiliary space : O(n+m) n = countMax.size, m = list.size
     */
    //array counts of every number
    val list = File(inputName).readLines().map { it.toInt() }
    val countMax = IntArray(list.max()!! + 1)
    list.forEach { it ->
        countMax[it]++
    }
    val max = countMax.max()
    var min = Int.MAX_VALUE
    countMax.forEachIndexed { index, it ->
        if (it == max && min > index)
            min = index
    }
    File(outputName).writer().run {
        list.forEach { it ->
            if (it != min)
                write("$it\n")
        }
        write("$min\n".repeat(max!!))
        close()
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    /**
     * time complexity : O(N)
     * auxiliary space : O(N)
     */
    var fIndex = 0
    var sIndex = first.size
    var index = 0
    while (fIndex < first.size && sIndex < second.size) {
        if (first[fIndex] < second[sIndex]!!) {
            second[index++] = first[fIndex++]
        } else {
            second[index++] = second[sIndex++]
        }
    }
    if (fIndex < first.size) {
        while (fIndex < first.size)
            second[index++] = first[fIndex++]
    } else if (sIndex < second.size) {
        while (sIndex < second.size)
            second[index++] = second[sIndex++]
    }
}


