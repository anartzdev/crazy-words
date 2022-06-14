package utils

import domain.interfaces.FileIF
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by anartzmugika on 5/10/17.
 */

class FileManage: FileIF {
    /**
     * Read select file info from select location
     * @param directory Select file directory
     */
    override fun readFile(directory: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Create file to save get Server info of select service
     * @param name: Location name
     * *
     * @param type: Type select service (Example: restaurant, hotel,...)
     * *
     * @param format: Format to create/read file
     * *
     * @param results : Found all service results
     */
    override fun createFile(name: String?, extension: String, directoryName: String, image: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun checkIfDirectoryExistAndIfNotExistCreate(directoryName: String) {
        //Add to create in C:// ---> Path "/Results"
        //Add inside project without "/"---> Results
        val path = Paths.get(directoryName)
        if (!Files.exists(path)) {
            println("NOT!! Exist")
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            println("Exist directory")
        }
    }
}