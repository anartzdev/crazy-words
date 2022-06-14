package main.numbers

/**
 * Created by anartzmugika on 26/10/17.
 */
import data.constants.MathConstants
import data.local.db.DbHelper
import extensions.addAnswers
import extensions.addDataToUseInGame
import java.util.ArrayList

/**
 * Created by anartzmugika on 22/10/17.
 */
fun main(args: Array<String>) {

    val db = DbHelper()

    val type = 2
    // 1: decimal_roman_numb_
    // 2: roman_decimal_numb_

    var question_type_prefix = ""
    if (type == 1) {
        question_type_prefix = "decimal_roman_numb_" // decimalNumbersToRoman
    } else if (type == 2) {
        question_type_prefix = "roman_decimal_numb_" // RomanToDecimalNumbers
    }

    // Query to get one million or more people cities
    val dataFromDB = db.executeQuery(MathConstants.RomanToDecimalNumbers)

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
                val question_ = dataFromDB.getString(2)

                val source = dataFromDB.getString(7)

                //ESTO CAMBIAR!!!
                val question_EN = "What number is it?"

                val questionDescription : ArrayList<String> = mutableListOf<String>() as ArrayList<String>

                if (type == 1) {
                    questionDescription.add("decimal_numbers_to_roman")
                    // questionDescription.add("cities_with_less_than_100_people")
                    questionDescription.add("Adivina el equivalente en números romanos")
                    questionDescription.add("Asmatu zenbaki erromatarretara bihurtuz")
                    questionDescription.add("What is the Roman numerals equivalent")
                } else {
                    questionDescription.add("roman_number_to_decimal")
                    // questionDescription.add("cities_with_less_than_100_people")
                    questionDescription.add("Adivina el equivalente en números")
                    questionDescription.add("Asmatu zenbaki arruntean bihurtuz")
                    questionDescription.add("What is the numerals equivalent")
                }

                val qUUID = "q_${question_EN.substring(0,question_EN.length).toLowerCase().replace(" ", "_").replace("?", "")}_${question_.replace("=","").replace(" ", "")}"
                val aUUID = "a_${dataFromDB.getString(1)}"

                val game: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                game.add((id))
                game.add((questionType))
                game.add((modality))
                game.add((questionDescription[0]))
                game.add((urlImg))
                game.add((qUUID))
                game.add((aUUID))
                game.add((source))

                val question: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
                question.add((qUUID))
                question.add("0")
                question.add(question_)
                question.add("")
                question.add("")


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