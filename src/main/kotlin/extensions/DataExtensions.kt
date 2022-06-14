package extensions

import data.local.db.DbHelper
import domain.models.Answer
import java.util.*
import kotlin.collections.ArrayList

/***********************************************************************************************************************
 * Created by Anartz Mugika on 16/11/17.
 **********************************************************************************************************************/

/*******************************************************************************************************************
 * Obtiene la respuestas dentro de un string para cuando son múltiples
 * De normal vendrá una respuesta pero puede ser que cuando haya más de un premiado en una categoría
 * (premio en algún deporte, reconocimiento compartido,...) estará concatenado con comas y lo que necesitaremos
 * será tener todas de manera individual en una lista para tratarlas de manera individual y poder sacar respuestas
 * incorrectas combinando con las correctas para darle complejidad
 *******************************************************************************************************************/
fun takeAllAnswersItemsFromString(answerStr: String): ArrayList<String> {
    val correctAnswerItems: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
    if (answerStr.contains(",")) {
        answerStr.split(",").map { value -> correctAnswerItems.add(value.trim()) }
    } else {
        correctAnswerItems.add(answerStr)
    }
    return correctAnswerItems
}

fun incorrectValues(champion: Answer, championship: String, gen: String, db: DbHelper, year: String = ""): ArrayList<Answer> {

    val champions = takeAllAnswersItemsFromString(champion.es)

    //Obtenemos los campeones ignorando los que estén en el ArrayList<String> champions
    val incorrectValues = findNotCorrectAnswers(db, champions, championship, gen, year)

    //TODO REVISARLO!!!!
    //Tiene que encontrarse por lo menos 3 incorrectas, si no...no sigue y no guardará esa pregunta
    //Hay que estudiar la manera de meter con diferentes descripciones pero que pertenezcan a una misma liga o competición
    //Esto pasa con la UCI World pro Tour, que están añadidas las carreras pero como tienen diferentes nombres no tiene 3 diferentes
    if (incorrectValues.size < 3)  {
        "*".logSection()
        "NOT GENERATE QUESTION BECAUSE NOT FIND 3 INCORRECT VALUES!!".log()
        "*".logSection()
        return ArrayList<Answer>()
    }

    val champsAnswerObjects = (mutableListOf<Answer>() as ArrayList<Answer>)
    champsAnswerObjects.add(champion)

    //Sigue si ha encontrado 3 incorrectas
    return manageIncorrectValuesToGenerateAnswersFalse(incorrectValues, champions, champsAnswerObjects)
}
fun manipulateIncorrectAnswers(answers: ArrayList<String>,
                               selectIncorrectValue: Int,
                               incorrectValues: ArrayList<Answer>): ArrayList<String> {
    val incorrectValuesListToManipulate = answers
    incorrectValuesListToManipulate.set(answers.size - 1, incorrectValues[selectIncorrectValue].es)
    Collections.shuffle(incorrectValuesListToManipulate)
    return incorrectValuesListToManipulate
}
