package com.example.joyfulcampus.ui.chat.chatdetail

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.databinding.ItemChatBinding
import com.example.joyfulcampus.ui.chat.userlist.UserItem
import com.google.firebase.firestore.FirebaseFirestore


class ChatDetailAdapter : ListAdapter<ChatDetailItem, ChatDetailAdapter.ViewHolder>(differ) {


    var otherUserItem: UserItem? = null

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val db = FirebaseFirestore.getInstance()

        //      채팅방 서로간의 채팅 내용 위치 변경
        fun bind(item: ChatDetailItem) {


            if (item.userId == otherUserItem?.userId) {
                if (item.imageUrl == null){
                    binding.chatImage.isVisible = false
                    binding.mychatImage.isVisible = false
                    binding.mymessageTextView.isVisible = false
                    binding.profileImageView.isVisible = true
                    Glide.with(binding.profileImageView).load(otherUserItem?.userprofileurl).into(binding.profileImageView)
                    binding.nicknameTextView.isVisible = true
                    binding.messageTextView.isVisible = true
                    binding.nicknameTextView.text = otherUserItem?.username
                    binding.messageTextView.text = item.message
                    binding.messageTextView.gravity = Gravity.START
                } else {
                    binding.chatImage.isVisible = true
                    binding.mychatImage.isVisible = false
                    binding.mymessageTextView.isVisible = false
                    binding.profileImageView.isVisible = true
                    Glide.with(binding.profileImageView).load(otherUserItem?.userprofileurl).into(binding.profileImageView)
                    binding.nicknameTextView.isVisible = true
                    binding.messageTextView.isVisible = false
                    binding.nicknameTextView.text = otherUserItem?.username
                    binding.chatImage
                    Glide.with(binding.chatImage)
                        .load(item.imageUrl)
                        .into(binding.chatImage)
                }
            } else {
                if (item.imageUrl == null){
                    binding.chatImage.isVisible = false
                    binding.mychatImage.isVisible = false
                    binding.mymessageTextView.isVisible = true
                    binding.profileImageView.isVisible = false
                    binding.nicknameTextView.isVisible = false
                    binding.messageTextView.isVisible = false
                    binding.mymessageTextView.text = item.message
                } else {
                    binding.chatImage.isVisible = false
                    binding.mychatImage.isVisible = true
                    binding.mymessageTextView.isVisible = false
                    binding.profileImageView.isVisible = false
                    binding.nicknameTextView.isVisible = false
                    binding.messageTextView.isVisible = false
                    binding.mychatImage
                    Glide.with(binding.mychatImage)
                        .load(item.imageUrl)
                        .into(binding.mychatImage)
                }
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
