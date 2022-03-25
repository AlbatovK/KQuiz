package com.albatros.kquiz.ui.adapter.answer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.databinding.AnswerLayoutBinding

class AnswerAdapter(
    private val answers: MutableList<String>,
    private val listener: AnswerAdapterListener
) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    override fun getItemCount(): Int = answers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding = AnswerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.answer = answers[position]
    }

    inner class AnswerViewHolder(private val binding: AnswerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var answer: String? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(answer: String?) {
            answer?.let {
                with(binding) {
                    data.text = it
                    root.setOnClickListener {
                        listener.onAnswerSelected(answer, answerCard)
                    }
                }
            }
        }
    }
}