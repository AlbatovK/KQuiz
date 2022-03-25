package com.albatros.kquiz.domain

import android.view.View
import android.view.animation.AnimationUtils

fun View.playFadeInAnimation(id: Int) {
    val context = this.context
    val animation = AnimationUtils.loadAnimation(context, id)
    this.startAnimation(animation)
    this.visibility = View.VISIBLE
}