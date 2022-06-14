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

                val nameAsccii = dataFromDB.getString(2) //English name
                val esName = dataFromDB.getString(3) //Spanish name
                val basqueName =  dataFromDB.getString(4)

                val id = "geo_world_flags_${nameAsccii}_${dataFromDB.getString(1)}".replace(" ", "_").replace("(", "").replace(")","").toLowerCase()
                val modality = "trivia,strangle"
                val questionType = "geo_world_flags_$modality".replace(",","_")
                val urlImg = "geography/flags/${dataFromDB.getString(1).toLowerCase()}.jpg"
                val questionES = "Bandera (${dataFromDB.getString(6)})"
                val questionEU = "Bandera (${dataFromDB.getString(6)})"
                val questionEN = "Flag (${dataFromDB.getString (6)})"
                val source = dataFromDB.getString(8)

                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                questionDescription.add("world_flags")
                // questionDescription.add("cities_with_less_than_100_people")
                questionDescription.add("Banderas del mundo")
                questionDescription.add("Munduko banderak")
                questionDescription.add("World flags")

                //Take incorrect options
                if (!dataFromDB.getString(5).equals("")) {
                    val selectIncorrectValues = incorrectValuesGeography(db, GeographyConstants.worldCapitalsFindIncorrectSQL(nameAsccii, dataFromDB.getString(6), dataFromDB.getString(7), true))

                    if (selectIncorrectValues.size == 3) {
                        val qUUID = "q_worlds_flags_${dataFromDB.getString(6).toLowerCase()}"
                        val aUUID = "a_worlds_flags_${dataFromDB.getString(1).toLowerCase()}"

                        println(urlImg)

                        val game = addGameData(listOf(id, questionType, modality, questionDescription[0], urlImg, qUUID, aUUID, source))

                        val question = addQuestions(listOf(qUUID, questionES, questionEU, questionEN))

                        val answerList = addAnswers(aUUID, "1",
                                listOf(
                                        (esName.removeUnusedChars()),
                                        selectIncorrectValues[0].nameES.removeUnusedChars(),
                                        selectIncorrectValues[1].nameES.removeUnusedChars(),
                                        selectIncorrectValues[2].nameES.removeUnusedChars()
                                ),
                                listOf(
                                        (basqueName.removeUnusedChars()),
                                        selectIncorrectValues[0].nameEU.removeUnusedChars(),
                                        selectIncorrectValues[1].nameEU.removeUnusedChars(),
                                        selectIncorrectValues[2].nameEU.removeUnusedChars()
                                ),
                                listOf(
                                        (nameAsccii.removeUnusedChars()),
                                        selectIncorrectValues[0].nameEN.removeUnusedChars(),
                                        selectIncorrectValues[1].nameEN.removeUnusedChars(),
                                        selectIncorrectValues[2].nameEN.removeUnusedChars()
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