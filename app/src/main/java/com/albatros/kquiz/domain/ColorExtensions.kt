package com.albatros.kquiz.domain

import android.graphics.Color

val colors = listOf(
    "#5643b5",
    "#78b327",
    "#cf9008",
    "#ba4747",
    "#b5ba29",
    "#089c99"
)

fun String.toColor() = Color.parseColor(this)

fun getRandomColor(): Int = colors.map(String::toColor).random()
