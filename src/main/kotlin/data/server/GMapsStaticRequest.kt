package data.server

import data.constants.GeographyConstants
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

/***********************************************************************************************************************
 * Created by Anartz Mugika on 15/7/17.
 * ---------------------------------------------------------------------------------------------------------------------
 * Estas clases servirán para obtener la información del servidor y añadirla directamente mediante la librería de GSON.
 * Después dentro de ForecastDataMapper se gestionará para modelarlo a nuestro gusto con las funciones correspondientes
 * y la definición que hemos realizado dentro del paquete "model" donde encontramos el fichero "DomainClasses" que es
 * donde se define la forma que tendrá la información al final del proceso de la petición
 ***********************************************************************************************************************/

class GMapsStaticRequest() {
    fun execute(lat: String, lng: String, mapType: String = "hybrid"): BufferedImage {
        return ImageIO.read(URL(GeographyConstants.IMAGE_MAP_STATIC(lat, lng, mapType = mapType)))
    }
}