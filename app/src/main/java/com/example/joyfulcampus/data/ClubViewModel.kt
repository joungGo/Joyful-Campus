package com.example.joyfulcampus.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClubViewModel : ViewModel() {

    // 소개 탭 데이터
    private val _introData = MutableLiveData<IntroData>()
    val introData: LiveData<IntroData> get() = _introData

    // 공고문 탭 데이터
    private val _noticeData = MutableLiveData<NoticeData>()
    val noticeData: LiveData<NoticeData> get() = _noticeData

    // 소개 데이터 저장
    fun saveIntroData(introData: IntroData) {
        _introData.value = introData
    }

    // 공고문 데이터 저장
    fun saveNoticeData(noticeData: NoticeData) {
        _noticeData.value = noticeData
    }
}

data class IntroData(
    val clubName: String,
    val clubDescription: String,
    val clubLongDescription: String
)

data class NoticeData(
    val clubName: String,
    val recruitmentPeriod: String,
    val interview: String,
    val noticeContent: String
)
