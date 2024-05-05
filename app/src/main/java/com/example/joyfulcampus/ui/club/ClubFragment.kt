package com.example.joyfulcampus.ui.club

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.FragmentClubBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ClubFragment : Fragment(R.layout.fragment_club) {
    private lateinit var binding: FragmentClubBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubBinding.bind(view)

        // 툴바 설정
        val toolbar = binding.clubToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true) // 프래그먼트에서 옵션 메뉴를 처리하기 위해 설정

        val db = Firebase.firestore
        db.collection("articles").document("xIkTdPh2aD7izK56Y9ad")
            .get()
            .addOnSuccessListener {result ->
                // 8-1. firestore에서 가져온 데이터가 저장된 ArticleModel를 Datatype으로 삼아 하나의 article 생성
                val article = result.toObject<ArticleModel>()
                Log.e("homeFragment", article.toString())
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.menu_club_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.alarm -> {
                // 알람 아이템 클릭 시 처리할 코드
                return true
            }
            R.id.find -> {
                // 찾기 아이템 클릭 시 처리할 코드
                return true
            }
            R.id.bookmark -> {
                // 북마크 아이템 클릭 시 처리할 코드
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
