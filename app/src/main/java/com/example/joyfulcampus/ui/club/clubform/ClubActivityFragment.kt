package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R

class ClubActivityFragment : Fragment(R.layout.fragment_club_activity) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_club_activity, container, false)
    }
}
