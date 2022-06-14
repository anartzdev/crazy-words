package data.constants

/**
 * Created by anartzmugika on 5/11/17.
 */
object DbConstants {
    //DATABASE Values
    private val USE_UNICODE = "?useUnicode=yes&characterEncoding=UTF-8&relaxAutoCommit=true"
    fun getDbUrl(dbName: String) = "jdbc:mysql://localhost:3306/$dbName$USE_UNICODE"
    val USER = "root"
    val PASS = ""
}