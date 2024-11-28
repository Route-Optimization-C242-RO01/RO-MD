package com.example.myrouteoptimization.ui.addroute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrouteoptimization.data.source.remote.response.PostDataItem
import com.example.myrouteoptimization.databinding.ItemRowDestinationBinding

class AddRouteAdapter(
    private val destinationList : MutableList<PostDataItem>,
    private val onDeleteClick : (Int) -> Unit
) : RecyclerView.Adapter<AddRouteAdapter.AddRouteViewHolder>(){
    inner class AddRouteViewHolder(private val binding : ItemRowDestinationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : PostDataItem, position: Int) {
            binding.tvDestination.text = item.street

            binding.ivClear.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRouteViewHolder {
        val binding = ItemRowDestinationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddRouteViewHolder(binding)
    }

    override fun getItemCount(): Int = destinationList.size

    override fun onBindViewHolder(holder: AddRouteViewHolder, position: Int) {
        holder.bind(destinationList[position], position)
    }
}