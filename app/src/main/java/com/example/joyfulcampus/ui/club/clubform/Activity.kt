package com.example.joyfulcampus.ui.club.clubform

data class Activity(
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis() // 기본값으로 현재 시간 추가
)
