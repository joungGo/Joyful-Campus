package com.example.joyfulcampus.ui.club.clubform.input

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentActivityInputBinding

class ActivityInputFragment : Fragment(R.layout.fragment_activity_input){
    private lateinit var binding: FragmentActivityInputBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActivityInputBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        // 필요한 추가 코드
    }
}
