package com.example.joyfulcampus.ui.home

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
import com.example.joyfulcampus.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // Toolbar 설정
        val toolbarBodyTemplate = view.findViewById<Toolbar>(R.id.homeToolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBodyTemplate)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈 버튼 활성화
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.list)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        // 텍스트 컬러를 다르게 적용할 문자열 생성
        val title = getString(R.string.편안한_Campus)
        val spannableTitle = SpannableStringBuilder(title)

        // "편안한" 부분을 굵게 처리 및 색상 적용
        val boldStyleSpan = StyleSpan(android.graphics.Typeface.BOLD)
        val redColor = ContextCompat.getColor(requireContext(), R.color.편안한)
        spannableTitle.setSpan(boldStyleSpan, 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableTitle.setSpan(ForegroundColorSpan(redColor), 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // "캠퍼스" 부분에 색상 적용
        val blueColor = ContextCompat.getColor(requireContext(), R.color.black)
        spannableTitle.setSpan(ForegroundColorSpan(blueColor), 4, title.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // SpannableStringBuilder을 툴바 타이틀로 설정
        toolbarBodyTemplate.title = spannableTitle
    }
}
