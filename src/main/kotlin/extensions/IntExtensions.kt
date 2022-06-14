package extensions


/**
 * Created by anartzmugika on 3/11/17.
 */
fun addDifferentNumbersToTakeSelectIncorrectValues(numbersTotal: Int, min: Int, max: Int) : List<Int> {
    val list = List(numbersTotal) { randomNumber(min, max + 1) }.distinct()
    println(list)
    if (list.size == numbersTotal) return list
    return addDifferentNumbersToTakeSelectIncorrectValues(numbersTotal, min, max)
}

private fun randomNumber(min: Int, max: Int): Int = (min..max).random()