package com.example.iotapplication.util

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.util.*


fun getProperty(key: String, context: Context): String {
    val property = Properties();
    val assertManager = context.assets
    var inStream: InputStream? = null
    try {
        inStream = assertManager.open("config.properties")
        property.load(inStream)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inStream?.close()
    }
    return property.getProperty(key)
}

enum class ButtonType() {
    LASER, LASER_PER, BUZZER, BUZZER_PER, COLLISION
}

enum class State(state: Int) {
    ON(1), OFF(0)
}