package extensions

import java.util.*

/**
 * Created by anartzmugika on 27/10/17.
 */
fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start