package extensions

/***********************************************************************************************************************
 * Created by anartzmugika on 7/11/17.
 ***********************************************************************************************************************/
fun <E> java.util.ArrayList<E>.arrayToStringFormat(): String = toString().replace("[", "").replace("]", "").replace(", ",",")