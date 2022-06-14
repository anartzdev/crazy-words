package main.geography

import data.constants.GeographyConstants
import data.local.db.DbHelper
import extensions.*

import java.util.ArrayList

/**
 * Created by anartzmugika on 22/10/17.
 */
fun main(args: Array<String>) {

    val db = DbHelper()

    // Query to get one million or more people cities
    val dataFromDB = db.executeQuery(GeographyConstants.worldCapitals)

    // RC.cioc, RC.name, RT.es, RT.eu, capital, region, subregion, url

    while(dataFromDB.next()) {
        println(" ${dataFromDB.getString(1)} / ${dataFromDB.getString(2)} / ${dataFromDB.getString(3)} / ${dataFromDB.getString(4)} / ${dataFromDB.getString(6)}")
        if(!dataFromDB.getString(6).equals("")) {
            try {

                val nameAsccii = dataFromDB.getString(2) //English name (use in all cases)

                val id = "geo_world_capitals_${nameAsccii}_${dataFromDB.getString(1)}".replace(" ", "_").replace("(", "").replace(")","").toLowerCase()
                val modality = "trivia,strangle"
                val questionType = "geo_world_capitals_$modality".replace(",","_")
                val urlImg = ""
                val questionES = "Capital de ${dataFromDB.getString(3)}"
                val questionEU = "${dataFromDB.getString(4)} herrialdeko hiriburua"
                val questionEN = "Capital of ${dataFromDB.getString (2)}"
                val source = dataFromDB.getString(8)

                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                questionDescription.add("world_capitals")
                // questionDescription.add("cities_with_less_than_100_people")
                questionDescription.add("Capitales del Mundo")
                questionDescription.add("Munduko herrialdeen kapitalak")
                questionDescription.add("World capitals")

                //Take incorrect options
                if (!dataFromDB.getString(5).equals("")) {
                    val selectIncorrectValues = incorrectValuesGeography(db, GeographyConstants.worldCapitalsFindIncorrectSQL(nameAsccii, dataFromDB.getString(6), dataFromDB.getString(7), true))

                    if (selectIncorrectValues.size == 3) {
                        val qUUID = "q_worlds_capitals_${dataFromDB.getString(1).toLowerCase()}"
                        val aUUID = "a_worlds_capitals_${dataFromDB.getString(1).toLowerCase()}"

                        val game = addGameData(listOf(id, questionType, modality, questionDescription [0], urlImg, qUUID, aUUID, source))

                        val question = addQuestions (listOf(qUUID, questionES, questionEU, questionEN))

                        val answerList = addAnswers (aUUID, "0",
                                listOf(
                                        (dataFromDB.getString(5).removeUnusedChars()),
                                        selectIncorrectValues[0].capital.removeUnusedChars(),
                                        selectIncorrectValues[1].capital.removeUnusedChars(),
                                        selectIncorrectValues[2].capital.removeUnusedChars()
                                ))

                        val dbInsertCapitals = DbHelper("crazy_sports")
                        addDataToUseInGame(dbInsertCapitals, questionDescription, question, answerList, game)
                        dbInsertCapitals.close()
                    }
                }

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    db.close();
}

