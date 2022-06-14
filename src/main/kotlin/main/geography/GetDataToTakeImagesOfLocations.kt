package main.geography

/**
 * Created by anartzmugika on 5/11/17.
 */
import data.constants.GeographyConstants
import data.constants.GeographyConstants.getSelectCountryRegion
import data.local.db.DbHelper
import domain.commands.RequestImageCommand
import extensions.*
import utils.FileManage
import java.io.File
import java.util.*
import javax.imageio.ImageIO


/**
 * Created by anartzmugika on 29/9/17.
 */

fun main(args: Array<String>) {
    val directoryName = "Results"
    val extension = "jpg"
    val hybrid = false
    var mapType = "roadmap"
    if (hybrid) { mapType = "hybrid"}
    val fileManage = FileManage()
    val downloadImages = false
    fileManage.checkIfDirectoryExistAndIfNotExistCreate(directoryName)

    val db = DbHelper()

    // Query to get one million or more people cities
    val dataFromDB = db.executeQuery(GeographyConstants.minOneMillionPopulation)

    while(dataFromDB.next()) {
        println(" ${dataFromDB.getString(1)} / ${dataFromDB.getString(2)} / ${dataFromDB.getString(3)} / ${dataFromDB.getString(4)} / ${dataFromDB.getString(6)}")
        val regionToSelect = db.executeQuery(getSelectCountryRegion(dataFromDB.getString(6).replace("'", ""), dataFromDB.getString(2).replace("'", ""), dataFromDB.getString(8).replace("'", "")))
        try {
            regionToSelect.next()
            println(regionToSelect.getString(1))

            val nameAsccii = dataFromDB.getString(2) //English name
            val esName = regionToSelect.getString(3) //Spanish name
            val basqueName =  regionToSelect.getString(4)
            val lat = dataFromDB.getString(3)
            val lng = dataFromDB.getString(4)
            val locationName = "geo_img_${mapType.toLowerCase()}_$nameAsccii (${dataFromDB.getString(6)})".replace(" ", "_").replace("(", "").replace(")","").toLowerCase()

            val id = locationName
            val modality = "trivia,strangle"
            val questionType = "geo_img_$modality".replace(",","_")
            val urlImg = "${locationName}.$extension".replace(" ", "_").replace("(", "_").replace(")","_").toLowerCase()
            val questionES = "¿Qué ciudad del continente de ${regionToSelect.getString(1)} es la que aparece en la imagen?"
            val questionEU = "Irudian agertzen den lurraldea ${regionToSelect.getString(1)} kontinenteko zein hiriburu da?"
            val questionEN = "What continent city of ${regionToSelect.getString (1)} is the one that appears in the image?"
            val source = createSourceUrlToGoogleMaps(lat, lng)

            if (downloadImages) {

                ImageIO.write(RequestImageCommand(lat, lng, mapType).execute(),
                        extension, File("$directoryName/${urlImg}"))
            }

            /***
             * We pass name of country / region / subregion
             */
            val selectIncorrectValues = incorrectValuesGeography(db, GeographyConstants.worldCapitalsFindIncorrectSQL(nameAsccii, regionToSelect.getString(1), regionToSelect.getString(5), false))

            val qUUID = "q_${nameAsccii.cleanString()}_${dataFromDB.getString(1)}_world_cp_img"
            val aUUID = "a_${nameAsccii.cleanString()}_${dataFromDB.getString(1)}_world_cp_img"


            val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>
            questionDescription.add("1_million_people_cities")
            // questionDescription.add("cities_with_less_than_100_people")
            questionDescription.add("Ciudades del mundo con + de 1000000 habitantes")
            questionDescription.add("1000000 biztanle baino gehiagoko munduko hiriak")
            questionDescription.add("World cities with more than 1000000 people")

            val game = addGameData(listOf(id, questionType, modality, questionDescription[0], urlImg, qUUID, aUUID, source))

            val question = addQuestions(listOf(qUUID, questionES, questionEU, questionEN), "1")


            //TODO Pending to refactor
            val answerList: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
            answerList.add((aUUID))
            answerList.add("1")
            answerList.add(("${dataFromDB.getString(2)} (${esName})"))
            answerList.add("${selectIncorrectValues[0].capital} (${selectIncorrectValues[0].nameES})")
            answerList.add("${selectIncorrectValues[0].capital} (${selectIncorrectValues[0].nameEU})")
            answerList.add("${selectIncorrectValues[0].capital} (${selectIncorrectValues[0].nameEN})")
            answerList.add(("${dataFromDB.getString(2)} (${basqueName})"))
            answerList.add("${selectIncorrectValues[1].capital} (${selectIncorrectValues[1].nameES})")
            answerList.add("${selectIncorrectValues[1].capital} (${selectIncorrectValues[1].nameEU})")
            answerList.add("${selectIncorrectValues[1].capital} (${selectIncorrectValues[1].nameEN})")
            answerList.add(("${dataFromDB.getString(2)} (${regionToSelect.getString(2)})"))
            answerList.add("${selectIncorrectValues[2].capital} (${selectIncorrectValues[2].nameES})")
            answerList.add("${selectIncorrectValues[2].capital} (${selectIncorrectValues[2].nameEU})")
            answerList.add("${selectIncorrectValues[2].capital} (${selectIncorrectValues[2].nameEN})")

            addDataToUseInGame(db, questionDescription, question, answerList, game)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    db.close();
}