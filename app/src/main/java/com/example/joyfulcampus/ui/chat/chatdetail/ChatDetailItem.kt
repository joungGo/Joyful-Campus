package com.example.joyfulcampus.ui.chat.chatdetail

data class ChatDetailItem (
//  chat
    var chatId: String? = null,
    val userId: String? = null,
    val message: String? = null,

    val articleId: String? = null,
    val createdAt: Long? = null,
    val imageUrl: String? = null,
)