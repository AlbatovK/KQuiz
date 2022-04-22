package com.albatros.kquiz.ui.adapter.client

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ResultClientLayoutBinding
import com.albatros.kquiz.model.data.info.ClientInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultAdapter(private val clients: MutableList<ClientInfo>, private val scope: CoroutineScope) :
    RecyclerView.Adapter<ResultAdapter.ResultClientViewHolder>() {

    companion object {
        private var lastRes: MutableList<ClientInfo>? = null

        fun clearLast() {
            lastRes = null
        }
    }

    override fun getItemCount(): Int = clients.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        lastRes = clients.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultClientViewHolder {
        val binding =
            ResultClientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultClientViewHolder, position: Int) {
        holder.client = if (lastRes == null) clients[position] else lastRes!![position]
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
            info?.let { last ->
                with(binding) {

                    root.animation = AnimationUtils.loadAnimation(root.context, R.anim.list_anim)
                    val new = clients.find { q -> q.id == last.id }!!

                    scope.launch {
                        delay(1200)
                        lastRes?.let {
                            if (it.indexOf(last) != clients.indexOf(new)) {
                                val from = it.indexOf(last)
                                val to = clients.indexOf(new)
                                it.removeAt(from).also { item ->
                                    it.add(to, item)
                                }
                                notifyItemMoved(from, to)
                            }
                        }
                    }

                    score.text = last.score.toString()

                    scope.launch(Dispatchers.Main) {
                        while (new.score > last.score) {
                            delay(10)
                            last.score += 10
                            score.text = last.score.toString()
                        }
                    }

                    name.text = last.name

                    val placeOrd = clients.map(ClientInfo::score).indexOf(new.score) + 1
                    place.text = if (placeOrd == 1) {
                        val placeHint = root.context.getString(R.string.place, placeOrd) + " \uD83D\uDC51"
                        placeHint
                    } else root.context.getString(R.string.place, placeOrd)

                    val from = if (new.questionMap.size == 0) 0 else new.questionMap.size - 1
                    val to = if (new.questionMap.size > 2) new.questionMap.size - 3 else 0
                    val lastCount = new.questionMap.filterKeys { it in from downTo to }.count { item -> item.value.right }

                    val hint = last.name + if (lastCount == 3) {
                        " \uD83D\uDD25"
                    } else if (new.score > last.score) {
                        " \uD83D\uDC4D"
                    } else if (new.score == last.score && lastRes != null) {
                        " \uD83D\uDE2D"
                    } else ""
                    name.text = hint
                }
            }
        }
    }
}