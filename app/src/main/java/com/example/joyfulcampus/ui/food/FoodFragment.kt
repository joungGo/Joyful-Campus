package com.example.joyfulcampus.ui.food

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentFoodBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class FoodFragment: Fragment(R.layout.fragment_food) {
    private lateinit var binding: FragmentFoodBinding
    private lateinit var firestore: FirebaseFirestore
    private var personCountListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFoodBinding.bind(view)
        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // Firestore에서 사람 수 리스닝 시작
        startListeningPersonCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 리스너 해제
        personCountListener?.remove()
    }

    private fun startListeningPersonCount() {
        val docRef = firestore.collection("person_count").document("current_count")
        personCountListener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val count = snapshot.getLong("count") ?: 0
                val timestamp = snapshot.getTimestamp("timestamp")

//              timestamp 뜨는 표기 바꾸기
                val date = timestamp?.toDate()
                val sdf = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초 z", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                val formattedDate = sdf.format(date)

                // UI 업데이트 코드 추가
                binding.countPerson.text = count.toString()
                binding.timestamp.text = formattedDate
            } else {
            }
        }

    }

}
