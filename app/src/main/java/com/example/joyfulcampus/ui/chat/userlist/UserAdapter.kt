package com.example.joyfulcampus.ui.chat.userlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.databinding.ItemUserBinding

class UserAdapter(private val onClick: (UserItem) -> Unit): ListAdapter<UserItem, UserAdapter.ViewHolder>(differ) {
    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserItem){
            binding.nicknameTextView.text = item.username
            binding.useremailTextView.text = item.useremail
            if (item.userprofileurl == "") else {
                Glide.with(binding.profileImageView).load(item.userprofileurl).into(binding.profileImageView)
                Log.d("userprofileurl","잘 나왔다")
            }
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}