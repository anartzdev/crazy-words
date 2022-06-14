package domain.models

import extensions.cleanString
import extensions.removeUnusedChars
import extensions.replaceSinqleQuote

/***********************************************************************************************************************
 * Created by Anartz Mugika on 9/11/17.
 **********************************************************************************************************************/
data class Game (val prefix : String,
                 val postfix: String,
                    val aUUID: String,                                  //Answer UUID
                    val modality: String,                               //trivia, strangle, order, multiple
                    val question : Question,
                    val questionDescriptionTexts: List<String>,
                    val source: String,                                 //Source to "justificate" answer
                    val urlImg: String = "",                            // Add url if image exist)
                    val extra: String,
                    val correctAnswer: Answer
) {
    /*************************************************************************************************************************
     * @description: En esta función creamos el tipo de pregunta haciendo uso del Identificador del juego, quitando el prefijo
     * del ID, concatenando la modalidad del jeugo y añadiendo el dato extra que normalmente será el valor que usemos para la
     * búsqueda. En el caso de los deportes será el valor de la columna "sport". El valor prefix es el que añadimos como prefijo,
     * en el caso de los "campeones" de los diferentes deportes "sports_champs"
     *************************************************************************************************************************/
    fun questionType() : String = "${GameID().replace("${prefix}_","")}_${modality}_${extra.cleanString()}"

    /*************************************************************************************************************************
     * @description: Esta función servirá para generar el ID del juego. Usará el prefix (que será sports_champs, o cualquier
     * prefijo que hemos usado como world_capitals y en el postfix, cogeremos el valor de la columna ID de la BBDD
     * para todo ello.
     *************************************************************************************************************************/
    fun GameID ():String = "${prefix}_${postfix.cleanString()}"
    fun QuestionES() : String = this.question.esText
    fun QuestionEU() : String = this.question.euText
    fun QuestionEN() : String = this.question.enText
    fun qUUID () = this.question.UUID.removeUnusedChars()
    fun aUUID () = this.aUUID.removeUnusedChars()
    fun getCorrectAnswer (lang: String): String {
        if (lang == "es") {
            return correctAnswer.es.replaceSinqleQuote()
        } else if (lang == "eu") {
            return correctAnswer.eu.replaceSinqleQuote()
        }
        return correctAnswer.en.replaceSinqleQuote()
    }

    /*******************************************************************************************************************
     * If answer contain basque answer we consider answer multi
     *******************************************************************************************************************/
    fun isMulti(): String = if (this.getCorrectAnswer("eu") != "") "1" else "0"
}

data class Question (val UUID: String, val multi: String, val esText: String, val euText: String = "", val enText: String = "" )

data class Answer(val es: String, val eu: String, val en: String)