package main
import data.constants.GeographyConstants
import data.constants.QuestionGeneratorConstantValues
import data.local.db.DbHelper
import domain.commands.RequestQuestionGeneratorCommand


/**********************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 4/10/17.
 * Take data from server and insert correctly in select table
 *********************************************************************************************************************/

fun main(args: Array<String>) {

    val result = RequestQuestionGeneratorCommand().execute()
    println("Total countries is ${result.size} countries.")
    /*result.forEach { country ->

    }*/

}

private fun showAllCountries() {
    val db = DbHelper()

    val dataFromDB = db.executeQuery(GeographyConstants.ALL_COUNTRIES)
    while(dataFromDB.next()) {
        println(" ${dataFromDB.getString(1)} / ${dataFromDB.getString(2)} / ${dataFromDB.getString(3)}")
    }
    db.close();
}

private fun addWikipediaURL() {
    val db = DbHelper();
    val dataFromDB = db.executeQuery(GeographyConstants.ALL_COUNTRIES_ONLY_NAME_CODE)
    while(dataFromDB.next()) {
        println(" ${dataFromDB.getString(1)} / ${dataFromDB.getString(2)} / ${createWikipediaURL(dataFromDB.getString(2))}")
        val list: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
        list.add(createWikipediaURL(dataFromDB.getString(2)))
        list.add(dataFromDB.getString(1))
        db.executeInsertUpdateOperation(GeographyConstants.addWikipediaURL, list)
    }
    db.close();
}

private fun createWikipediaURL(name:String): String {
    return "${QuestionGeneratorConstantValues.WIKIPEDIA_URL}${name.replace(" ", "_")}"
}