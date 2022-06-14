package main

import data.local.db.DbHelper
import data.constants.QuestionGeneratorConstantValues
import java.util.ArrayList

/**
 * Created by anartzmugika on 29/10/17.
 */

/***************************************************************************************
//ESTE VALOR HAY QUE CAMBIARLO SI USAMOS OTRA BBDD!!!!!!!
 ***************************************************************************************/
private val USE_DB_TO_EXTRACT_DATA = "ahorkadoa"
private val USE_DB_TO_ADD_DATA = "crazy_sports"
fun main(args: Array<String>){

    //Abrir la BBDD donde vamos a extraer datos para llevarlos a 'data'
    val db = DbHelper(USE_DB_TO_EXTRACT_DATA)
    // Consulta de la tabla que queremos obtener los datos para usarlos a la hora de  importar los datos a "data"
    val dataFromDB = db.executeQuery("SELECT * FROM `game_questions` GQ, game_words GW WHERE GQ.word_slug = GW.slug AND topic_slug = 'videogames' LIMIT 0,5")
    /*******************************************************************************************************************************
    El orden de los datos para añadirlos en la tabla 'data' son los siguientes y así habrá que añadirlos en el ArrayList<String>
     ********************************************************************************************************************************/
    val data: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
    while(dataFromDB.next()) {
        //topic_slug
        //question_eu
        //question_es
        //question_en
        //word_slug
        //difficult
        //slug (lo mismo que world_slug)
        //eu
        //es
        //en
        //url

        //Esto es editable o si tenemos en la tabla donde cogemos los datos, usamos sus datos
        val desc_en = "Videogames questions"
        val desc_es = "Preguntas acerca de videojuegos"
        val desc_eu = "Bideojokoei buruzko galderak"


        data.add(dataFromDB.getString(8)) //Respuesta en euskera, en esta caso da igual el idioma aunque para algunas tipo de preguntas hay que ampliarlo pero por ahora se queda asi
        data.add(desc_en)
        data.add(desc_es)
        data.add(desc_eu)
        data.add("") //GENERO EN
        data.add("") //GENERO ES
        data.add("") //GENERO EU
        data.add("${dataFromDB.getString(1)}_${dataFromDB.getString(5)}") //CONCAT topic_slug con slug
        data.add(dataFromDB.getString(4)) //PREGUNTA EN INGLES
        data.add(dataFromDB.getString(3)) //PREGUNTA EN ESPAÑOL
        data.add(dataFromDB.getString(2)) //PREGUNTA EN EUSKERA
        data.add(dataFromDB.getString(11))
        data.add("videogames") // "deporte"
        data.add("") //AÑO
        // (`answer`, `desc_en`, `desc_es`,  `desc_eu`, `gen_en`, `gen_es`, `gen_eu`, `id`, `question_EN`, `question_ES`, `question_EU`, `sport`, `year`) " +

        //BBDD donde vamos a introducir los datos en la tabla 'data'
        val dbInsertToDataTable = DbHelper(USE_DB_TO_ADD_DATA)
        dbInsertToDataTable.executeInsertUpdateOperation(QuestionGeneratorConstantValues.insertValuesDataInDataTable, data)
        dbInsertToDataTable.close()
        data.clear()
    }
    db.close()
}