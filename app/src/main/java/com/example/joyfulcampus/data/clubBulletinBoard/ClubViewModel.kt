package com.example.joyfulcampus.data.clubBulletinBoard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.joyfulcampus.data.clubBulletinBoard.IntroData

class ClubViewModel : ViewModel() {

    private val _introData = MutableLiveData<IntroData>()
    val introData: LiveData<IntroData> get() = _introData

    fun saveIntroData(data: IntroData) {
        _introData.value = data
    }
}
