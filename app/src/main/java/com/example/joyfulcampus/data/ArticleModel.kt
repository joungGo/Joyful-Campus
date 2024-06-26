package com.example.joyfulcampus.data

// firestore에서 가져온 data를 임시로 저장하는 공간(Model)
data class ArticleModel (
    val articleId: String? = null,
    val createdAt: Long? = null,
    val clubNameText: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val type: String? = null,
)
