package com.albatros.kquiz.ui.adapter.answer

import android.view.View
import androidx.cardview.widget.CardView

interface AnswerAdapterListener {
    fun onAnswerSelected(answer: String, cardView: CardView)
}