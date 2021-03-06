package com.anufriev.utils.common

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.anufriev.utils.R
import kotlinx.android.synthetic.main.custom_toast_layout.view.*

class KCustomToast {
    companion object {
        val GRAVITY_TOP = 48
        val GRAVITY_CENTER = 17
        val GRAVITY_BOTTOM = 80

        private lateinit var layoutInflater: LayoutInflater

        fun infoToast(context: Activity, message: String, position: Int):Toast {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(
                R.layout.custom_toast_layout, (context).findViewById(
                    R.id.custom_toast_layout
                )
            )
            layout.custom_toast_image.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_information
                )
            )
            val drawable = ContextCompat.getDrawable(context, R.drawable.toast_round_background)
            drawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.colorPaleAccent
                ), PorterDuff.Mode.DARKEN
            )
            layout.background = drawable
            layout.custom_toast_message.setTextColor(Color.WHITE)
            layout.custom_toast_message.text = message
            val toast = Toast(context.applicationContext)
            toast.duration = Toast.LENGTH_LONG
            if (position == GRAVITY_BOTTOM) {
                toast.setGravity(position, 0, 20)
            } else {
                toast.setGravity(position, 0, 0)
            }
            toast.view = layout //setting the view of custom toast layout
            toast.show()
            return toast
        }
    }
}