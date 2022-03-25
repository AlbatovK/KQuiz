package com.albatros.kquiz.ui.adapter.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.QuizLayoutBinding
import com.albatros.kquiz.domain.playFadeInAnimation
import com.albatros.kquiz.model.data.Quiz

class QuizAdapter(
    private val quizzes: MutableList<Quiz>,
    private val listener: QuizAdapterListener,
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun getItemCount(): Int = quizzes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) { holder.quiz = quizzes[position] }

    inner class QuizViewHolder(private val binding: QuizLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var quiz: Quiz? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(quiz: Quiz?) {
            quiz?.let {
                with(binding) {
                    title.text = it.name
                    val qString = root.context.resources.getQuantityString(R.plurals.question_plurals, it.questions.size)
                    count.text = root.context.getString(R.string.question_size, it.questions.size, qString)
                    topics.text = it.topics.joinToString(", ")

                    root.playFadeInAnimation(R.anim.fade_in_animation)
                    root.setOnClickListener { view ->
                        try { listener.onQuizSelected(it, view) } catch (e: Exception) {}
                    }
                }
            }
        }
    }
}