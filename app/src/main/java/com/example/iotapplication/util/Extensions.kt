package com.example.iotapplication.util

import android.content.Context
import android.widget.Button
import com.example.iotapplication.R


fun Button.ON(context: Context) {
    this.setBackgroundColor(context.resources.getColor(R.color.accent))
    this.setTextColor(context.resources.getColor(R.color.primary_light))
    this.setText(R.string.on)
}

fun Button.OFF(context: Context) {
    this.setBackgroundColor(context.resources.getColor(R.color.transparent))
    this.setTextColor(context.resources.getColor(R.color.accent))
    this.setText(R.string.off)
}

