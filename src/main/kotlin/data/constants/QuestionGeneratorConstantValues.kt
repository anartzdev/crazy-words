package data.constants

/***********************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 13/10/17.
 * Define use all constants. URL LOCALHOST to get API data and Database connections and queries
 ***********************************************************************************************************************/
class QuestionGeneratorConstantValues() {
    companion object {
        //CACHE
        val THIRTY_MINUTES_CACHE_AGE = "30000"
        val READ_TIME_IN_MS = 20000
        val CONNECT_TIME_IN_MS = 20000

        val WIKIPEDIA_URL = "https://en.m.wikipedia.org/wiki/"

        val insertDescription = "INSERT IGNORE INTO `description_question` (`id`, `es`, `eu`, `en`)  " +
                "VALUES ( ?, ? , ?, ?)"

        val insertAnswer = "INSERT IGNORE INTO `answer` (`id`, `multi`, `correct_es`, `incorrect_1_es`, `incorrect_2_es`, `incorrect_3_es`,`correct_eu`, `incorrect_1_eu`, `incorrect_2_eu`, `incorrect_3_eu`,`correct_en`, `incorrect_1_en`, `incorrect_2_en`, `incorrect_3_en`)  " +
                "VALUES ( ?, ? , ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)"

        val insertQuestion = "INSERT IGNORE INTO `question` (`id`, `multi`, `es`, `eu`, `en`)  " +
                "VALUES ( ?, ? , ?, ?, ?)"

        val insertGameData = "INSERT IGNORE INTO `game` (`id`, `question_type`, `modality`,  `description`, `img`, `question`, `answer`, `source`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

        // Consulta para introducir los datos seleccionados en la tabla 'data' que será la que usemos para generar las preguntas desde las diferentes opciones
        // (Por ahora solo implementado y adaptado para los deportes)

        val insertValuesDataInDataTable = "INSERT IGNORE INTO `data` (`answer`, `desc_en`, `desc_es`,  `desc_eu`, `gen_en`, `gen_es`, `gen_eu`, `id`, `question_EN`, `question_ES`, `question_EU`, `source`, `sport`, `year`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

    }



}