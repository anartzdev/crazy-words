package domain.commands

import data.server.QuestionGeneratorRequest
import domain.models.RestCountries

/**********************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 4/10/17.
 * Make server request
 *********************************************************************************************************************/


class RequestQuestionGeneratorCommand(): Command<List<RestCountries>> {
    override fun execute(): List<RestCountries> = QuestionGeneratorRequest().execute()
}