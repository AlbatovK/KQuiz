package com.albatros.kquiz.ui.adapter.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.databinding.ClientLayoutBinding
import com.albatros.kquiz.model.data.ClientInfo

class ClientAdapter(private val clients: MutableList<ClientInfo>) :
    RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun getItemCount(): Int = clients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding =
            ClientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.client = clients[position]
    }

    inner class ClientViewHolder(private val binding: ClientLayoutBinding) :
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
                    name.text = it.name
                }
            }
        }
    }
}