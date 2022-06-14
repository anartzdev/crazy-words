package main.numbers

import data.constants.MathConstants
import data.local.db.DbHelper
import extensions.addAnswers
import extensions.addDataToUseInGame
import extensions.addGameData
import extensions.addQuestions
import java.util.ArrayList

/**
 * Created by anartzmugika on 26/10/17.
 */
fun main(args: Array<String>) {
    val db = DbHelper()

    val type = 2
    // 1: decimal_roman_numb_
    // 2: roman_decimal_numb_

    val question_type_prefix: String
    if (type == 1) {
        question_type_prefix = "roman_operations_q_" // romanOperations
    } else {
        question_type_prefix = "math_operations_q_"
    }

    // Query to get one million or more people cities
    val dataFromDB = db.executeQuery(MathConstants.mathOperations)

    // RC.cioc, RC.name, RT.es, RT.eu, capital, region, subregion, url

    while(dataFromDB.next()) {
        println(" ${dataFromDB.getString(1)} / ${dataFromDB.getString(2)} / ${dataFromDB.getString(3)} / ${dataFromDB.getString(4)} / ${dataFromDB.getString(6)}")
        if(!dataFromDB.getString(6).equals("")) {
            /*val regionToSelect = db.executeQuery(Constants.getSelectCountryRegion(dataFromDB.getString(6).replace("'", ""), dataFromDB.getString(2).replace("'", ""), dataFromDB.getString(8).replace("'", "")))
        */try {


                val id = "math_${dataFromDB.getString(1)}"
                val modality = "trivia"
                val questionType = "$question_type_prefix$modality".replace(",","_")
                val urlImg = ""
                val questionText = dataFromDB.getString(2)

                val source = dataFromDB.getString(7)

                //ESTO CAMBIAR!!!
                val question_EN = "What number is it?"

                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>

                if (type == 1) {
                    questionDescription.add("roman_operations_desc")
                    // questionDescription.add("cities_with_less_than_100_people")
                    questionDescription.add("Adivina el resultado de la operación en número romanos")
                    questionDescription.add("Asmatu zenbaki erromatarretako eragiketa")
                    questionDescription.add("What is the result of the Roman number operation")
                } else {
                    questionDescription.add("math_numbers_operations_desc")
                    // questionDescription.add("cities_with_less_than_100_people")
                    questionDescription.add("Adivina el resultado de la operación matemática")
                    questionDescription.add("Asmatu eragiketa honen emaitza")
                    questionDescription.add("What is the result of the mathematical operation")
                }

                val qUUID = "q_${question_EN.substring(0,question_EN.length).toLowerCase().replace(" ", "_").replace("?", "")}_${questionText.replace("=","").replace(" ", "")}"
                val aUUID = "a_${dataFromDB.getString(1)}"

                val game = addGameData(listOf(id, questionType, modality, questionDescription [0], urlImg, qUUID, aUUID, source))

                val question = addQuestions (listOf(qUUID, questionText, "", "", "0"))

                val answerList = addAnswers(aUUID, "0",
                        listOf<String>(dataFromDB.getString(3),
                        dataFromDB.getString(4),
                        dataFromDB.getString(5),
                        dataFromDB.getString(6)))

                addDataToUseInGame(db, questionDescription, question, answerList, game)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    db.close();
}