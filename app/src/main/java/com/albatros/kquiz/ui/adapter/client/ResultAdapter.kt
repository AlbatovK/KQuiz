package com.albatros.kquiz.ui.adapter.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ResultClientLayoutBinding
import com.albatros.kquiz.domain.playFadeInAnimation
import com.albatros.kquiz.model.data.info.ClientInfo

class ResultAdapter(private val clients: MutableList<ClientInfo>) :
    RecyclerView.Adapter<ResultAdapter.ResultClientViewHolder>() {

    override fun getItemCount(): Int = clients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultClientViewHolder {
        val binding =
            ResultClientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultClientViewHolder, position: Int) {
        holder.client = clients[position]
    }

    inner class ResultClientViewHolder(private val binding: ResultClientLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var client: ClientInfo? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(info: ClientInfo?) {
            info?.let {
                with(binding) {

                    binding.root.playFadeInAnimation(R.anim.fade_in_animation)
                    name.text = it.name

                    val scr = it.questionMap.values.filter { item -> item.right }.sumOf {
                        1200 - it.time * 10
                    }

                    score.text = scr.toString()

                    place.text = root.context.getString(R.string.place, clients.map {
                            item -> item.questionMap.values.sumOf { q -> if (q.right) 1200 - q.time * 10 else 0 }
                    }.indexOf(scr) + 1)

                    val from = if (it.questionMap.size == 0) 0 else it.questionMap.size - 1
                    val to = if (it.questionMap.size > 2) it.questionMap.size - 3 else 0
                    val lastCount = it.questionMap.filterKeys { it in from downTo to }.count { item -> item.value.right }

                    if (lastCount == 3) {
                        val hint = it.name + " \uD83D\uDD25"
                        name.text = hint
                    }
                }
            }
        }
    }
}