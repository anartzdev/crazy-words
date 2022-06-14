package main.sports

import data.constants.SportConstants
import data.local.db.DbHelper
import domain.models.Answer
import domain.models.Game
import domain.models.Question
import extensions.*
import kotlin.collections.ArrayList

/**********************************************************************************************************************
 * @author: Anartz Mugika (mugan86@gmail.com)
 * @description: En esta clase vamos a gestionar toda la información de los deportes y generar las preguntas que le
 * corresponden como puede ser campeones de X competiciones de deportes como Fútbol, Tenis, Motor,...
 * O premios individuales como número 1 de la ATP en Tenis, Pichichis de Fútbol de las diferentes ligas,...
 * @created: 26/10/17.
 * @lastUpdate: 17/11/07
 *********************************************************************************************************************/

class ChampionsQuestions(val sportSelect: String) {
    /***************************************************************************************
    //ESTE VALOR HAY QUE CAMBIARLO SI USAMOS OTRA BBDD!!!!!!!
     ***************************************************************************************/
    private val USEDB = "crazy_sports"
    val consoleLog = true

    fun init () {
        val db = DbHelper(USEDB)
        //En el parámetro pasamos el deporte que vamos a añadir (el campo "sport" en la tabla SQL data)
        val dataFromDB = db.executeQuery(String.format(SportConstants.champsData, sportSelect))
        while(dataFromDB.next()) {
            try {
                /************************************************************************************************
                 * ESTO ES PERSONALIZABLE AUNQUE EN LOS DEPORTES NO HABRÍA QUE CAMBIAR NADA
                 **********************************************************************************************/
                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                questionDescription.add(dataFromDB.getString(4).cleanString())
                questionDescription.add(dataFromDB.getString(4))
                questionDescription.add(dataFromDB.getString(5))
                questionDescription.add(dataFromDB.getString(6))

                val gameData = Game(aUUID = "a_${dataFromDB.getString(3).cleanString()}",
                        modality = "trivia,strangle",
                        question = Question(UUID = "q_${dataFromDB.getString(3).cleanString()}",
                                multi = "1",
                                esText = dataFromDB.getString(7),
                                euText = dataFromDB.getString(8),
                                enText = dataFromDB.getString(9)),
                        questionDescriptionTexts = listOf(questionDescription[0], questionDescription[1], questionDescription[2], questionDescription[3]),
                        source = dataFromDB.getString(13),
                        prefix = "sport_champs",
                        postfix = dataFromDB.getString(3).cleanString(),
                        extra = dataFromDB.getString(2), //Añadimos el deporte del que estamos filtrando
                        correctAnswer = Answer(dataFromDB.getString(10),
                                dataFromDB.getString(11),
                                dataFromDB.getString(12))
                )

                if (consoleLog) {
                    gameData.qUUID().log()
                    gameData.aUUID().log()
                    gameData.questionType().log()
                }

                /***********************************************************************************************************
                 * Parámetros que se pasan.
                 * (1): Se pasa la respuesta correcta en un objecto Answer que contiene los 3 idiomas.
                 * (2): Descripción de la competición.
                 * (3): Género.
                 * (4): El objecto de la BBDD local para las operaciones SQL.
                 * (5): Se pasa el año, en casos como ligas que contienen NBA o ABA, se usará este valor, si no...pues no.
                 ***********************************************************************************************************/
                val selectIncorrectValues = incorrectValues(gameData.correctAnswer, dataFromDB.getString(4), dataFromDB.getString(14), db, dataFromDB.getString(1).cleanString())

                if (selectIncorrectValues.size != 0) {

                    val game = addGameData(listOf(gameData.GameID(),
                            gameData.questionType(), gameData.modality,
                            gameData.questionDescriptionTexts[0],
                            gameData.urlImg, gameData.question.UUID, gameData.aUUID(), gameData.source))

                    val question = addQuestions (listOf(gameData.qUUID(), gameData.QuestionES(), gameData.QuestionEU(), gameData.QuestionEN()))

                    val answerList = addAnswers(gameData.aUUID(), gameData.isMulti(),
                            listOf(
                                    (gameData.getCorrectAnswer("es").removeUnusedChars()),
                                    selectIncorrectValues[0].es.removeUnusedChars(),
                                    selectIncorrectValues[1].es.removeUnusedChars(),
                                    selectIncorrectValues[2].es.removeUnusedChars()
                            ),
                            listOf(
                                    (gameData.getCorrectAnswer("eu").removeUnusedChars()),
                                    selectIncorrectValues[0].eu.removeUnusedChars(),
                                    selectIncorrectValues[1].eu.removeUnusedChars(),
                                    selectIncorrectValues[2].eu.removeUnusedChars()
                            ),
                            listOf(
                                    (gameData.getCorrectAnswer("en").removeUnusedChars()),
                                    selectIncorrectValues[0].en.removeUnusedChars(),
                                    selectIncorrectValues[1].en.removeUnusedChars(),
                                    selectIncorrectValues[2].en.removeUnusedChars()
                            ))

                    addDataToUseInGame(db, questionDescription, question, answerList, game)
                }
                selectIncorrectValues.clear()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        db.close();
    }
}
