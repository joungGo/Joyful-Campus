package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubFreeBinding

class ClubFreeFragment : Fragment(R.layout.fragment_club_free) {
    private lateinit var binding: FragmentClubFreeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubFreeBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        // Firestore에서 데이터를 로드하고 UI를 업데이트하는 코드 추가
    }

    companion object {
        fun newInstance(clubId: String) = ClubFreeFragment().apply {
            arguments = Bundle().apply {
                putString("clubId", clubId)
            }
        }
    }
}
