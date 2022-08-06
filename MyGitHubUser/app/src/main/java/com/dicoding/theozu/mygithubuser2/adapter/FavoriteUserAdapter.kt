package com.dicoding.theozu.mygithubuser2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.theozu.mygithubuser2.R
import com.dicoding.theozu.mygithubuser2.database.FavoriteUser
import com.dicoding.theozu.mygithubuser2.databinding.ItemRowUserBinding

class FavoriteUserAdapter(private val listUser: List<FavoriteUser>) : RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUser) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
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
        fun onItemClicked(data: FavoriteUser)
    }
}