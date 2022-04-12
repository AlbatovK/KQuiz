package com.albatros.kquiz.domain

import android.view.View
import android.view.animation.AnimationUtils

fun View.playFadeInAnimation(id: Int) {
    with(this) {
        val animation = AnimationUtils.loadAnimation(context, id)
        startAnimation(animation)
        visibility = View.VISIBLE
    }
}