package com.dicoding.theozu.mygithubuser2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.theozu.mygithubuser2.R
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import com.dicoding.theozu.mygithubuser2.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: List<ItemsItem>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .placeholder(R.drawable.ic_person_grey)
                    .circleCrop()
                    .into(imgPhoto)

                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}