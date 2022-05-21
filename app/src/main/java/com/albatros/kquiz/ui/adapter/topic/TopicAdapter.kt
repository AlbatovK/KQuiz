package com.albatros.kquiz.ui.adapter.topic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.databinding.TopicLayoutBinding

class TopicAdapter(
    private var topics: MutableList<String>,
    private val listener: TopicAdapterListener
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    override fun getItemCount(): Int = topics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = TopicLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    @Suppress("NotifyDataSetChanged")
    fun setSource(topics: List<String>) {
        this.topics = topics.toMutableList()
        notifyDataSetChanged()
    }

    fun addTopics(topic: String) {
        topics.add(topic)
        notifyItemInserted(topics.lastIndex)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.topic = topics[position]
    }

    inner class TopicViewHolder(private val binding: TopicLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var topic: String? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(topic: String?) {
            topic?.let {
                with(binding) {
                    topicTxt.text = topic

                    if (topics.indexOf(topic) == 0) {
                        root.setOnClickListener {
                            listener.onLastCardSelected(root)
                        }
                    }
                }
            }
        }
    }
}