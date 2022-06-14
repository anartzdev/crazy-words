package data.local.db

import data.constants.DbConstants
import data.constants.DbConstants.getDbUrl
import java.sql.Connection
import java.sql.DriverManager

/***********************************************************************************************************************
 * Created by Anartz Mugika (mugan86@gmail.com) on 13/10/17.
 * Initialize MySQL connection
 ***********************************************************************************************************************/

class Connection{
    fun connectToDB(databaseName: String): Connection? {
        try {
            //Load MySQL driver
            Class.forName("com.mysql.jdbc.Driver")
            //Make connection with database
            return DriverManager.getConnection(getDbUrl(databaseName), DbConstants.USER, DbConstants.PASS)
        } catch (e: Exception) {
            return null
        }
    }
}