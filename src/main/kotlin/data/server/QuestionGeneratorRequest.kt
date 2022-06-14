package data.server

import com.google.gson.Gson
import domain.models.RestCountries
import java.net.URL
import com.google.gson.reflect.TypeToken
import data.constants.GeographyConstants

/******************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 04/10/2017.
 * Take data from server
 ******************************************************************************************************************/
class QuestionGeneratorRequest() {

    fun execute() : List<RestCountries> = Gson().fromJson(URL(GeographyConstants.URL_LOCALHOST).readText(), object : TypeToken<List<RestCountries>>() {}.type)
}