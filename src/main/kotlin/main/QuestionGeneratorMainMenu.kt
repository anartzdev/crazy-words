package main

import data.constants.SportConstants
import main.sports.ChampionsQuestions


/**
 * Created by anartzmugika on 15/11/17.
 */
fun main(args: Array<String>) {

    for (i in 0..SportConstants.existSport.size - 1) {
        ChampionsQuestions(SportConstants.existSport[i]).init()
    }

}