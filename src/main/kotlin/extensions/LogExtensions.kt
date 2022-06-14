package extensions

/**
 * Created by anartzmugika on 15/11/17.
 */

// HEAD OR FOOTER LINE WITH CUSTOM CHARS
//For example: "*".logSection(40) ---> "****************************************..."
fun String.logSection(n: Int= 100) = println(repeat(n))

fun option(opt: Int, description: String) = println("*${opt}: ${description}")

fun String.log()= println(this)
