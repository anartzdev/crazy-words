package data.constants

import extensions.arrayToStringFormat
import extensions.filtNotAnswerConditions

/**********************************************************************************************************************
 * @author: Fernando Gabriel,Anartz Mugika (mugan86@gmail.com)
 * @description: ...
 * @created: 03/11/17.
 * @lastUpdate: 23/11/07
 *********************************************************************************************************************/
object SportConstants {

    val champsData = "SELECT year,sport, id, desc_es, desc_eu, desc_en, question_ES, question_EU, question_EN, answer_es, answer_eu, answer_en, source, gen_en FROM `data` WHERE sport='%s'" //  WHERE sport='Football'

    private val noChampsSQLStartExpression = "SELECT DISTINCT(answer_es), answer_eu, answer_en FROM `data` WHERE "

    fun findOtherYearsChampions (champion: String, championship: String, gen: String = ""): String {
        return "SELECT DISTINCT(answer_es), answer_eu, answer_en FROM `data` WHERE answer_es <> '${champion}' AND desc_es = '$championship' AND gen_en = '$gen'"
    }

    fun findOtherYearsChampionsWithYear(champions: ArrayList<String>, championship: String, year: String, gen: String = "", genCheck: Boolean = false): String {

        val inStr = when {
            championship.contains("NBA") -> "'${championship}', '${championship.replace("NBA", "ABA")}', '${championship.replace("NBA", "BAA")}'"
            championship.contains("ABA") -> "'${championship}', '${championship.replace("ABA", "NBA")}', '${championship.replace("ABA", "BAA")}'"
            championship.contains("BAA") -> "'${championship}', '${championship.replace("BAA", "NBA")}', '${championship.replace("BAA", "ABA")}'"
            else -> ""
        }

        val filtNotAnswer = filtNotAnswerConditions(champions = champions)

        var sql = if (inStr != "") {
            "$noChampsSQLStartExpression $filtNotAnswer AND desc_es IN ($inStr) "
        } else {
            "$noChampsSQLStartExpression $filtNotAnswer AND desc_es = '$championship'"
        }

        if (genCheck) {
            sql = "$sql' AND gen_en = '$gen'"
        }

        sql = sql.replace("WHERE   AND", "WHERE ")

        return "$sql ORDER BY ABS(year-$year)"
    }

    fun findOtherYearsChampions(champions: ArrayList<String>, championship: String, gen: String): String =
            "$noChampsSQLStartExpression answer_es <> '${champions.arrayToStringFormat()}' AND desc_es = '$championship' AND gen_en = '$gen'"

    val existSport = listOf<String>("Sports car", "Basque Pelota", "Handball", "Cycling", "Golf", "Motociclismo", "Rallys", "Basketball", "Tenis", "Triathlon", "Basket", "Rugby", "Baseball", "Football")
}