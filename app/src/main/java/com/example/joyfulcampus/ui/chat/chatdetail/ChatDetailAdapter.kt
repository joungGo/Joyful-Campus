package com.example.joyfulcampus.ui.chat.chatdetail

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.ItemChatBinding
import com.example.joyfulcampus.ui.chat.userlist.UserItem
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database


class ChatDetailAdapter : ListAdapter<ChatDetailItem, ChatDetailAdapter.ViewHolder>(differ) {

    var otherUserItem: UserItem? = null

    val currentUserId = Firebase.auth.currentUser?.uid ?: ""
    val currentUserDB = Firebase.database.reference.child(Key.DB_USERS).child(currentUserId)


    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatDetailItem) {
            if (item.userId == otherUserItem?.userId) {
                binding.profileImageView.isVisible = true
                binding.nicknameTextView.isVisible = true
                binding.nicknameTextView.text = otherUserItem?.username
                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.START
            } else {
                binding.profileImageView.isVisible = false
                binding.nicknameTextView.isVisible = false
                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.END
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<ChatDetailItem>() {
            override fun areItemsTheSame(
                oldItem: ChatDetailItem,
                newItem: ChatDetailItem
            ): Boolean {
                return oldItem.chatId == newItem.chatId

            }

            override fun areContentsTheSame(
                oldItem: ChatDetailItem,
                newItem: ChatDetailItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}
