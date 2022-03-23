package com.albatros.kquiz.ui.adapter.quiz

import android.view.View
import com.albatros.kquiz.model.data.Quiz

interface QuizAdapterListener {
    fun onQuizSelected(quiz: Quiz, view: View)
}