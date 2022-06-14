package data.constants

/**
 * Created by anartzmugika on 5/11/17.
 */
object GeographyConstants {
    val URL_LOCALHOST = "https://restcountries.eu/rest/v2/all"
    private val GOOGLE_MAPS_URL_LOCALHOST = "https://maps.googleapis.com/maps/api/staticmap"
    private val API_KEY = "AIzaSyDjSQp1NtdHgtAZT2_3gQADTnmpeppYdk8"
    fun IMAGE_MAP_STATIC(lat: String, lng: String, mapType: String = "roadmap") = "$GOOGLE_MAPS_URL_LOCALHOST?key=$API_KEY&center=$lat,$lng&zoom=13&format=png&maptype=$mapType&style=feature:administrative.locality%7Celement:labels.text%7Cvisibility:off&size=600x480"

    val minOneMillionPopulation = "SELECT * FROM `words_world_cities_table` WHERE pop > 1000000"

    val maxOneHundredPopulation = "SELECT * FROM `words_world_cities_table` WHERE pop < 100 AND country <> 'Antarctica'"

    val ALL_COUNTRIES = "SELECT * FROM `rescountries_country`"
    val ALL_COUNTRIES_ONLY_NAME_CODE = "SELECT cioc, name FROM `rescountries_country`"

    val addWikipediaURL = "UPDATE `rescountries_country` SET `url` = ? WHERE `rescountries_country`.`cioc` = ?"

    // fun getSelectCountryRegion(country: String, capital: String, code: String) = "SELECT region FROM `rescountries_country` WHERE name = '$country' OR capital = '$capital' OR alpha3Code = '$code' "

    fun getSelectCountryRegion(country: String, capital: String, code: String) = "SELECT region, name, es, eu, subregion FROM `rescountries_country` RC, restcountries_translations RT WHERE  RT.cioc = RC.cioc AND (name = '$country' OR capital = '$capital' OR alpha3Code = '$code')"

    val worldCapitals = "SELECT RC.cioc, RC.name, RT.es, RT.eu, capital, region, subregion, url FROM rescountries_country RC, restcountries_translations RT WHERE RT.cioc = RC.cioc"

    fun worldCapitalsFindIncorrectSQL(correctCapital: String, region: String = "", subregion: String = "", filtBySubRegion: Boolean): String {
        var regionSub: String
        if (filtBySubRegion) {  //Find using subregion
            regionSub = " AND subregion='${subregion}' AND name <> '${correctCapital}' AND name <> ''"
        } else {
            regionSub = " AND region='${region}' AND name <> '${correctCapital}' AND name <> ''"
        }
        return "${worldCapitals}${regionSub}"
    }

    val  withoutBordersCountries = "SELECT RC.cioc, RC.name, RT.es, RT.eu, capital, region, subregion, url FROM rescountries_country RC, restcountries_translations RT WHERE borders = '' AND RC.cioc = RT.cioc"
    val countryWithBorders = withoutBordersCountries.replace("= ''", "<> ''")

    fun takei18nRegionsSubregionsTexts(subregion: String) = "SELECT region, subregion, region_es, subregion_es, region_eu, subregion_eu FROM `rescountries_country` RC, `restcountries_region_translates` RRT WHERE RRT.subregion_en = RC.subregion AND RC.subregion= '${subregion}' GROUP BY region, subregion"
    /*****************************************************************************************************************
     * INSERT QUERIES TO RESTCOUNTRIES API
     */
    val insertValue = "INSERT IGNORE INTO `rescountries_country` (`cioc`, `name`, `topLevelDoimain`, `alpha2Code`, `alpha3Code`, `callingCodes`, `capital`, `altSpellings`, `region`, `subregion`, `population`, `lat`, `lng`, `demonym`, `area`, `gini`, `timezones`, `borders`, `nativeName`, `numericCode`, `flag`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"

    val insertTranslations = "INSERT IGNORE INTO `restcountries_translations` (`cioc`, `de`, `es`, `fr`, `ja`, `it`, `br`, `pt`, `nl`, `hr`, `fa`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

    val insertRegionalBlocks = "INSERT IGNORE INTO `restcountries_regionalbloc` (`cioc`, `acronym`, `name`, `otherAcronyms`, `otherNames`)" +
            "VALUES (?, ?, ?, ?, ?)"

    val insertLanguages = "INSERT IGNORE INTO `restcountries_language` (`cioc`, `iso639_1`, `iso639_2`, `name`, `nativeName`)" +
            "VALUES (?, ?, ?, ?, ?)"

    val insertCurrencies = "INSERT IGNORE INTO `restcountries_currency` (`cioc`, `code`, `name`, `symbol`)" +
            "VALUES (?, ?, ?, ?)"
}