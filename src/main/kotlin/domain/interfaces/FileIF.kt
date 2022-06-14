package domain.interfaces

/**
 * Created by anartzmugika on 5/11/17.
 */
interface FileIF {

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
    fun createFile(name: String?, extension: String, directoryName: String, image: Boolean)

    /**
     * Read select file info from select location
     * @param directory Select file directory
     */
    fun readFile(directory: String)


}