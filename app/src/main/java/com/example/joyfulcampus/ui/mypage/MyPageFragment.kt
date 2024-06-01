package com.example.joyfulcampus.ui.mypage

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.FragmentMypageHomeBinding
import com.example.joyfulcampus.ui.auth.AuthActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.rpc.context.AttributeContext.Auth

class MyPageFragment: Fragment(R.layout.fragment_mypage_home) {
    private lateinit var binding: FragmentMypageHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageHomeBinding.bind(view)

        mypagetoolbar()

//      Firebase realtime database 가져올 위치 설정
        val myUserId = Firebase.auth.currentUser?.uid ?: ""
        val userDB = Firebase.database.reference.child(Key.DB_USERS).child(myUserId)

//      이름 가져오기
        userDB.child("username").get().addOnSuccessListener {
            val username = it.getValue(String::class.java)
            binding.usernametext.text = username ?: "No username found"
        }

//      학과 가져오기


//      아이디(이메일) 가져오기
        userDB.child("useremail").get().addOnSuccessListener {
            val useremail = it.getValue(String::class.java)
            binding.useridtextview.text = useremail ?: "No userid found"
        }

//      로그아웃
        binding.logoutframelayout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent)
        }

    }


    private fun mypagetoolbar() {
        // Toolbar 설정
        val toolbarBodyTemplate = view?.findViewById<Toolbar>(R.id.mypagetoolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBodyTemplate)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈 버튼 활성화
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.list)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        // 텍스트 컬러를 다르게 적용할 문자열 생성
        val title = getString(R.string.편안한_Campus)
        val spannableTitle = SpannableStringBuilder(title)

        // "편안한" 부분을 굵게 처리 및 색상 적용
        val boldStyleSpan = StyleSpan(Typeface.BOLD)
        val redColor = ContextCompat.getColor(requireContext(), R.color.편안한)
        spannableTitle.setSpan(boldStyleSpan, 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableTitle.setSpan(ForegroundColorSpan(redColor), 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // "캠퍼스" 부분에 색상 적용
        val blueColor = ContextCompat.getColor(requireContext(), R.color.black)
        spannableTitle.setSpan(ForegroundColorSpan(blueColor), 4, title.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // SpannableStringBuilder을 툴바 타이틀로 설정
        toolbarBodyTemplate?.title = spannableTitle
    }
}