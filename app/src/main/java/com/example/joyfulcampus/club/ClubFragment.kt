package com.example.joyfulcampus.club

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubBinding

class ClubFragment: Fragment(R.layout.fragment_club) {
    private lateinit var binding: FragmentClubBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubBinding.bind(view)
    }
}