package com.albatros.kquiz.ui.adapter.question

import androidx.cardview.widget.CardView
import com.albatros.kquiz.model.data.pojo.Question

interface QuestionAdapterListener {
    fun onDelete(question: Question, cardView: CardView)
}