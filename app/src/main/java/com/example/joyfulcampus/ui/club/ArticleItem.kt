package com.example.joyfulcampus.ui.club

data class ArticleItem(
    val articleId: String,
    val clubNameText: String,
    val description: String,
    val imageUrl: String,
    var isBookMark: Boolean
)