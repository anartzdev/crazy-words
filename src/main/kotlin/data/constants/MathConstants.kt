package data.constants

/**
 * Created by anartzmugika on 5/11/17.
 */
object MathConstants {
    val decimalNumbersToRoman = "SELECT * FROM math WHERE id LIKE 'decimal_numbers_to_roman%'"

    val RomanToDecimalNumbers = "SELECT * FROM `math` WHERE id LIKE 'roman_%' AND id NOT LIKE 'roman_numbers_operations%'"

    val romanOperations = "SELECT * FROM `math` WHERE id LIKE 'roman_numbers_operations_roman_res_%'"

    val mathOperations = "SELECT * FROM `math` WHERE id LIKE 'math_operations%' ORDER BY `correct` ASC"

}