package com.albatros.kquiz.ui.adapter.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.QuestionLayoutBinding
import com.albatros.kquiz.model.data.pojo.Question

class QuestionAdapter(private var questions: MutableList<Question>, private val listener: QuestionAdapterListener) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun getItemCount(): Int = questions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = QuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @Suppress("NotifyDataSetChanged")
    fun setSource(questions: List<Question>) {
        this.questions = questions.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.question = questions[position]
    }

    inner class QuestionViewHolder(private val binding: QuestionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var question: Question? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(question: Question?) {
            question?.let {
                with(binding) {

                    description.text = question.description
                    right.text = question.answer
                    answers.text = question.variants.joinToString(",\n")
                    counter.text = root.context.getString(
                        R.string.q_count, questions.indexOf(it) + 1
                    )
                }
            }
        }
    }
}