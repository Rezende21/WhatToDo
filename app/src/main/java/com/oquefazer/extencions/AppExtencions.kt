package com.oquefazer.extencions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

fun Date.format() : String {
    return SimpleDateFormat("dd/MM/yyyy", locale).format(this)
}

var TextInputLayout.text : String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun convertDataToLong(minuteLong : Long, hour : Long) : Long {
    //convert hora to miliseconds
    var horaToMinutes = hour * 60
    horaToMinutes *= 60
    horaToMinutes *= 1000

    // convert minuts to miliseconds
    var minutetominuts = minuteLong * 60
    minutetominuts *= 1000
    return horaToMinutes+minutetominuts
}