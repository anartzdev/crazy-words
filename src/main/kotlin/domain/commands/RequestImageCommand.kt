package domain.commands

import data.server.GMapsStaticRequest
import java.awt.image.BufferedImage

/**
 * Created by anartzmugika on 5/11/17.
 */
class RequestImageCommand(val lat: String, val lng: String, val mapType: String = "hybrid") : Command<BufferedImage> {
    override fun execute(): BufferedImage {
        return GMapsStaticRequest().execute(lat, lng, mapType)
    }
}