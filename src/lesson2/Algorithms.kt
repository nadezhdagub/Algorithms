@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    /**
     * time complexity : O(N*logN) N is list.size
     * auxiliary space : O(N) N is list.size
     */

    val list = File(inputName).readLines().map { it.toInt() }.toIntArray()
    val deltas = list.zip(list.copyOfRange(1, list.size)) { it, next -> next - it }.toIntArray()
    val max = maxSubArray(deltas, 0, deltas.size - 1)
    return max[1] + 1 to max[2] + 2
}

fun maxSubArray(list: IntArray, left: Int, right: Int): IntArray {
    if (left == right) return intArrayOf(list[left], left, left)
    val mid = (left + right) / 2
    val leftSum = maxSubArray(list, left, mid)
    val rightSum = maxSubArray(list, mid + 1, right)
    val crossSum = crossSubArray(list, left, mid, right)

    return if (leftSum[0] >= rightSum[0] && leftSum[0] >= crossSum[0])
        leftSum
    else if (rightSum[0] >= leftSum[0] && rightSum[0] >= crossSum[0])
        rightSum
    else
        crossSum
}

fun crossSubArray(list: IntArray, left: Int, mid: Int, right: Int): IntArray {
    var leftSum = Int.MIN_VALUE
    var sum = 0
    var L = 0
    for (index in mid downTo left) {
        sum += list[index]
        if (sum > leftSum) {
            L = index
            leftSum = sum
        }
    }
    var rightSum = Int.MIN_VALUE
    sum = 0
    var R = 0
    for (index in mid + 1..right) {
        sum += list[index]
        if (sum > rightSum) {
            R = index
            rightSum = sum
        }

    }
    return intArrayOf(leftSum + rightSum, L, R)
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int = josephus(menNumber, choiceInterval, 1)

/**
 * time complexity : O(N)? in worst situation (when not first 2 lines in josephus)
 * auxiliary space : O(N) N is menNumber
 */

fun josephus(menNumber: Int, choiceInterval: Int, start: Int): Int {
    if (menNumber == 1 || (choiceInterval == 2 && menNumber % 2 == 1)) return 1
    if (choiceInterval == 1) return menNumber
    val newSp = (start + choiceInterval - 2) % menNumber + 1
    val survivor = josephus(menNumber - 1, choiceInterval, newSp)
    return if (survivor < newSp) {
        survivor
    } else
        survivor + 1
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
fun longestCommonSubstring(first: String, second: String): String {
    /**
     * time complexity : O(n*m)
     * auxiliary space : O(n)
     */

    val m = first.length
    val n = second.length

    var result = 0
    var end = 0
    val len = Array(2) { IntArray(n) }

    var currRow = 0

    for (i in 0 until m) {
        for (j in 0 until n) {
            if (i == 0 || j == 0) {
                len[currRow][j] = 0
            } else if (first[i - 1] == second[j - 1]) {
                len[currRow][j] = len[1 - currRow][j - 1] + 1
                if (len[currRow][j] > result) {
                    result = len[currRow][j]
                    end = i - 1
                }
            } else {
                len[currRow][j] = 0
            }
        }

        currRow = 1 - currRow
    }
    if (result == 0) return ""
    return first.substring(end - result + 1, end + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */

//решето Эратосфена
fun calcPrimesNumber(limit: Int): Int = if (limit >= 1) isPrime(limit) else 0

/**
 * time complexity : O(N logN(logN))  N is limit
 *  auxiliary space : O(N) N is limit
 */

fun isPrime(limit: Int): Int {
    val prime = BooleanArray(limit + 1) { true }
    prime[0] = false
    var index = 2
    var index2: Int
    while (Math.pow(index.toDouble(), 2.0) <= limit) {
        if (prime[index]) {
            index2 = index
            while (index * index2 <= limit) {
                prime[index2 * index] = false
                index2++
            }
        }
        index++
    }
    return prime.count { it } - 1
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    /**
     * time complexity : ???
     * auxiliary space : ???
     */
    TODO()
}