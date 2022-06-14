package data.local.db

import java.sql.Statement
import java.sql.Connection
import java.sql.ResultSet

/***********************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 13/10/17.
 * Manage Local DB operations
 ***********************************************************************************************************************/
class DbHelper (databaseName : String = "restcountries"){
    private var connection: Connection?

    init {
        connection = Connection().connectToDB(databaseName)
    }

    fun executeQuery(sql: String): ResultSet = (connection?.createStatement() as Statement).executeQuery(sql)

    fun executeInsertUpdateOperation(sql: String, listParameters : ArrayList<String>) {
        val statement = connection?.prepareStatement(sql)
        var i = 0
        listParameters.map {
            statement?.setString((i + 1), listParameters.get(i))
            i++
        }
        statement?.update()
    }

    fun close() {
        connection?.close()
    }
}