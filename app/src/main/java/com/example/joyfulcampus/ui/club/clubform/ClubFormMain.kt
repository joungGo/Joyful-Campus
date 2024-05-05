package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubformMainBinding


class ClubFormMain: Fragment(R.layout.fragment_clubform_main) {
    private lateinit var binding: FragmentClubformMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubformMainBinding.bind(view)
    }
}