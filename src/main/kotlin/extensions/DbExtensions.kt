package extensions

import data.constants.QuestionGeneratorConstantValues
import data.constants.SportConstants
import data.local.db.DbHelper
import domain.models.Answer
import domain.models.Country
import java.sql.ResultSet

/**********************************************************************************************************************
 * @author: Fernando Gabriel,Anartz Mugika (mugan86@gmail.com)
 * @description: ...
 * @created: 03/11/17.
 * @lastUpdate: 18/11/07
 *********************************************************************************************************************/
fun addGameData (gameDataElements: List<String>): ArrayList<String> {
    val game: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
    game.add(gameDataElements [0])
    game.add(gameDataElements [1])
    game.add(gameDataElements [2])
    game.add(gameDataElements [3]) //Se añade el ID de la tabla de questions_description
    game.add(gameDataElements [4]) //Se añade una url de la image si existiese
    game.add(gameDataElements [5])
    game.add(gameDataElements [6])
    game.add(gameDataElements [7]) //Fuente de la respuesta
    return game
}

fun addQuestions(questionElements: List<String>, multi: String = "1"): ArrayList<String> {
    val question: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
    question.add(questionElements [0])
    question.add(multi) //La pregunta SI es Multi por usar "1"
    question.add(questionElements [1])
    question.add(questionElements [2])
    question.add(questionElements [3])
    return question
}

fun addAnswers(a_uuid: String, multi: String = "0", answers_es: List<String>, answers_eu: List<String> = listOf(), answers_en: List<String> = listOf()): ArrayList<String> {
    val answerList: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
    answerList.add(a_uuid)
    answerList.add(multi) //En este caso, para todos los idiomas se usa la misma respuesta
    answerList.add(answers_es[0])  // RESPUESTA CORRECTA (Sería en ES aunque en este caso al ser "multi=0" es para todos igual)
    answerList.add(answers_es[1])    //RESPUESTA INCORRECTA 1
    answerList.add(answers_es[2])    //RESPUESTA INCORRECTA 2
    answerList.add(answers_es[3])    //RESPUESTA INCORRECTA 3
    if (multi == "1") {
        answerList.add(answers_eu[0])
        answerList.add(answers_eu[1])
        answerList.add(answers_eu[2])
        answerList.add(answers_eu[3])
        answerList.add(answers_en[0])
        answerList.add(answers_en[1])
        answerList.add(answers_en[2])
        answerList.add(answers_en[3])
    } else {
        for (i in 0 until 8) {
            answerList.add("")
        }
    }
    return answerList
}

fun addDataToUseInGame(db: DbHelper, questionDescription: ArrayList<String>, question: ArrayList<String>, answerList: ArrayList<String>, game: ArrayList<String>) {
    //Introducir los datos en este orden en la BBDD
    db.executeInsertUpdateOperation(QuestionGeneratorConstantValues.insertDescription, questionDescription)
    db.executeInsertUpdateOperation(QuestionGeneratorConstantValues.insertQuestion, question)
    db.executeInsertUpdateOperation(QuestionGeneratorConstantValues.insertAnswer, answerList)
    db.executeInsertUpdateOperation(QuestionGeneratorConstantValues.insertGameData, game)
}

fun findNotCorrectAnswers(db: DbHelper, champions: ArrayList<String>, championship: String, gen: String, year: String = ""): ArrayList<Answer> {
    //Obtenemos los campeones ignorando los que estén en el ArrayList<String> champions
    "+".logSection()
    championship.log()
    "+".logSection()

    val notCorrectAnswersFind :ResultSet = db.executeQuery(takeNotCorrectItemsSql(champions,championship, gen, year));

    val incorrectValues: ArrayList<Answer> = mutableListOf<Answer>() as ArrayList<Answer>

    //Cargamos los valores incorrectos excepto las respuestas correctas
    while (notCorrectAnswersFind.next()) {
        incorrectValues.add(Answer(notCorrectAnswersFind.getString(1), notCorrectAnswersFind.getString(2), notCorrectAnswersFind.getString(3)))
    }
    return incorrectValues
}

fun manageIncorrectValuesToGenerateAnswersFalse(incorrectValues: ArrayList<Answer>,
                                                champions: ArrayList<String>,
                                                champs: ArrayList<Answer>, consoleLog: Boolean = true): ArrayList<Answer> {

    val loadSelectIncorrectValues: ArrayList<Answer> = mutableListOf<Answer>() as ArrayList<Answer>
    addDifferentNumbersToTakeSelectIncorrectValues(3, 0, incorrectValues.size - 1).map { value ->
        //Cuando son premios compartidos (2 ó más)
        if (champions.size > 1) {
            val incorrectValuesListToManipulateES = manipulateIncorrectAnswers(takeAllAnswersItemsFromString(champs[0].es), value, incorrectValues)
            val incorrectValuesListToManipulateEU = manipulateIncorrectAnswers(takeAllAnswersItemsFromString(champs[0].eu), value, incorrectValues)
            val incorrectValuesListToManipulateEN = manipulateIncorrectAnswers(takeAllAnswersItemsFromString(champs[0].en), value, incorrectValues)

            if (consoleLog) incorrectValuesListToManipulateES.toString().log()

            loadSelectIncorrectValues.add(Answer(incorrectValuesListToManipulateES.arrayToStringFormat(),
                                                    incorrectValuesListToManipulateEU.arrayToStringFormat(),
                                                        incorrectValuesListToManipulateEN.arrayToStringFormat()))
        } else {
            //Sigue si ha encontrado 3 incorrectas
            // Seleccionar 3 incorrectas de todas las diposnibles
            loadSelectIncorrectValues.add(Answer(incorrectValues[value].es,
                                                        incorrectValues[value].eu, incorrectValues[value].en))
        }
    }
    if (consoleLog) "SHOW TAKE INCORRECT VALUES: ${loadSelectIncorrectValues}".log()
    return loadSelectIncorrectValues
}


/***********************************************************************************************************************
 * Use in Geography type questions
 ***********************************************************************************************************************/
fun incorrectValuesGeography(db: DbHelper, sqlString: String): ArrayList<Country> {

    val loadIncorrectValues = db.executeQuery(sqlString)
    val incorrectValues: ArrayList<Country> = mutableListOf<Country>() as ArrayList<Country>
    while(loadIncorrectValues.next()) {
        incorrectValues.add(Country(loadIncorrectValues.getString(1), loadIncorrectValues.getString(2), loadIncorrectValues.getString(3),
                loadIncorrectValues.getString(4), loadIncorrectValues.getString(5), loadIncorrectValues.getString(6),
                loadIncorrectValues.getString(7), loadIncorrectValues.getString(8)))
    }
    //Tiene que encontrarse por lo menos 3 incorrectas, si no...no sigue y no guardará esa pregunta
    if (incorrectValues.size < 3) {
        return ArrayList<Country>()
    }

    val loadSelectIncorrectValues: ArrayList<Country> = mutableListOf<Country>() as ArrayList<Country>
    addDifferentNumbersToTakeSelectIncorrectValues(3, 0, incorrectValues.size - 1).map {
        value ->
        loadSelectIncorrectValues.add(incorrectValues[value])
    }
    return loadSelectIncorrectValues

}

/*******************************************************************************************************************
 * En esta función conseguimos los valores para usarlos como filtro ya que son respuestas correctas 1,N y no deben de
 * aparecer bajo ningún concepto.
 ********************************************************************************************************************/
fun filtNotAnswerConditions(champions: ArrayList<String>): String {
    var filtNotAnswer = ""
    champions.map {
        championAnswer -> filtNotAnswer = "$filtNotAnswer AND answer_es <> '$championAnswer'"
    }
    if(filtNotAnswer.substring(0,3) == "AND") {
        filtNotAnswer = filtNotAnswer.substring(3)
    }
    return filtNotAnswer
}

private fun takeNotCorrectItemsSql(champions: ArrayList<String>, championship: String, gen: String, year: String): String {
    val sql: String
    if (championship.indexOf("NBA") == -1 && championship.indexOf("NBA") == -1) {
        "Normal".log()
        sql = SportConstants.findOtherYearsChampions(champions, championship, gen)

    } else {
        "NO NORMAL".log()
        sql = SportConstants.findOtherYearsChampionsWithYear(champions, championship, year, gen)
    }

    "+".logSection()
    sql.log()
    "+".logSection()
    return sql
}
