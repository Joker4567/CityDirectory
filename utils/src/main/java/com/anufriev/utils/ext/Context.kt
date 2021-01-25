package com.anufriev.utils.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.getCompatColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.getCompatDrawable(@DrawableRes drawable: Int) =
    AppCompatResources.getDrawable(this, drawable)

fun Context.goToPhoneDial(phone: String = "89885438819", code:Int, fragment:Fragment) {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
    fragment.startActivityForResult(intent, code)
}