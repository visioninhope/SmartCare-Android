package dev.atick.compose.utils

import dev.atick.compose.data.OxiMeterData

fun MutableList<OxiMeterData>.toCsv(): String {
    val sb = StringBuilder()
    forEach {
        sb.append(it.toString())
        sb.append("\n")
    }

    return sb.toString()
}