package com.anufriev.utils.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getShortPhone(number:String) : String {
    var numberClear = number.filter { x -> x.isDigit() }
    if(numberClear.length > 10)
        return numberClear.substring(1)
    return numberClear
}