package com.anufriev.utils.ext

import android.content.Context
import android.location.LocationManager
import android.text.TextUtils
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

fun String.isPhoneValid(): Boolean =
    !TextUtils.isEmpty(this) && android.util.Patterns.PHONE.matcher(this)
        .matches()

fun getGPS(context:Context): Boolean {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}