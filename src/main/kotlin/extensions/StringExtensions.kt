package extensions

import java.util.*

/***********************************************************************************************************************
 * @author: Anartz Mugika (mugan86@gmail.com)
 * @description: String extensions to clean in other classes code.
 * @created: 13/10/17.
 * @lastUpdate: 05/11/07
 ***********************************************************************************************************************/
fun cleanStringResult(value: String) =  value.replace("[", "").replace("]", "")

fun countSelectCharInString (word:String, findChar: Char): Int {
    var countAppearances = 0;
    for (i in 0 until word.length) {
        if (word.get(i) == findChar) {
            countAppearances+= 1
        }
    }
    return countAppearances
}

fun CharSequence.toChar() = single()

fun String.removeUnusedChars() = replace("[b]","").replace("'", "").replace("´","").replace("__","_").replace("men", "m").replace("women","w")

fun String.cleanString() = trim().toLowerCase().replace(" ", "_").replace(" / ","_").replace("/", "_")
                                .replace("___", "_").replace("_-_","_").replace(",","_").trim().removeUnusedChars()

fun String.questionUUIDClean() = cleanString().replace("?", "").replace("=","")

fun String.onlyFirstChar(upper: Boolean = false) = when {
    !upper -> substring(0,1).toLowerCase()
    else -> {
        substring(0,1).toUpperCase()
    }
}

fun String.replaceSinqleQuote() = replace("'", "´")

fun createSourceUrlToGoogleMaps (lat: String, lng: String) = "https://www.google.com/maps/?q=$lat,$lng"

fun generateUUID() : String = UUID.randomUUID().toString()

fun String.convertToWikipediaMobile() : String = String().replace("en.wikipedia", "en.m.wikipedia").replace("es.wikipedia", "es.m.wikipedia").replace("eu.wikipedia", "eu.m.wikipedia")