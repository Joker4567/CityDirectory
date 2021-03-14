package com.anufriev.utils.ext

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.LayoutRes

fun View.show() = run { visibility = View.VISIBLE }

fun View.gone() = run { visibility = View.GONE }

fun View.enable() = run { isEnabled = true }

fun View.disable() = run { isEnabled = false }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Spinner.onItemSelected(emptyVoid: (String) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            emptyVoid.invoke(parent?.getItemAtPosition(position).toString())
        }
    }
}

//TextView -> ссылка. Например: ссылка на сайт или ссылка на октрытие чего-либо
inline fun TextView.setupLink(
    text: String,
    link: String,
    normalColor: Int,
    pressedColor: Int,
    crossinline func: () -> Unit
) {

    if (!text.contains(link) || text.isEmpty() || link.isEmpty()) {
        this.text = text
        return
    }

    val ssb = SpannableStringBuilder(text)

    val startIndex = text.indexOf(link)
    val lastIndex = startIndex + link.length


}