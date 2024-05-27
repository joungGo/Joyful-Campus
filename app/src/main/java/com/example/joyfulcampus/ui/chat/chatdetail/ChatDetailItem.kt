package com.example.joyfulcampus.ui.chat.chatdetail

data class ChatDetailItem (
    var chatId: String? = null,
    val userId: String? = null,
    val message: String? = null,

    val articleId: String? = null,
    val createdAt: Long? = null,
    val clubNameText: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
//  chat
    val chatNameText: String? = null,
)