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
    val dataFromDB = db.executeQuery(GeographyConstants.withoutBordersCountries)

    // RC.cioc, RC.name, RT.es, RT.eu, capital, region, subregion, url

    while(dataFromDB.next()) {
        if(!dataFromDB.getString(6).equals("")) {
            try {

                val nameAsccii = dataFromDB.getString(2) //English name
                val esName = dataFromDB.getString(3) //Spanish name
                val basqueName =  dataFromDB.getString(4)

                val id = "geo_world_country_no_borders_${nameAsccii}_${dataFromDB.getString(1)}".replace(" ", "_").replace("(", "").replace(")","").toLowerCase()
                val modality = "trivia,strangle"
                val questionType = "geo_world_country_no_borders_$modality".replace(",","_")
                val urlImg = ""
                val questionES = "¿Cuál de estos paises de NO tiene frontera? (Será una isla en general)"
                val questionEU = "Hauetako zein herrialdek EZ dauka beste herrialdekiko muga? (Irla izango da normalean)"
                val questionEN = "Which of these NO countries has a border? (It will be an island in general)"
                val source = dataFromDB.getString(8)

                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                questionDescription.add("world_country_no_borders")
                // questionDescription.add("cities_with_less_than_100_people")
                questionDescription.add("Países del mundo SIN fronteras")
                questionDescription.add("Beste herrialdekiko mugarik EZ daukan munnduko herrialdeak")
                questionDescription.add("Countries of the world WITHOUT borders")

                //Take incorrect options
                if (!dataFromDB.getString(5).equals("")) {
                    val selectIncorrectValues = incorrectValuesGeography(db, GeographyConstants.countryWithBorders)

                    if (selectIncorrectValues.size == 3) {
                        val qUUID = "q_world_country_no_borders_${dataFromDB.getString(1).toLowerCase()}"
                        val aUUID = "a_world_country_no_borders_${dataFromDB.getString(1).toLowerCase()}"

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