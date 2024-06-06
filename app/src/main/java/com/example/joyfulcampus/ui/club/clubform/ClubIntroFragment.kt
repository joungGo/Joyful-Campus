package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubIntroBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClubIntroFragment : Fragment(R.layout.fragment_club_intro) {
    private lateinit var binding: FragmentClubIntroBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubIntroBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        // 데이터 로드
        Firebase.firestore.collection("bulletinBoard").document(clubId)
            .get()
            .addOnSuccessListener { document ->
                val clubName = document.getString("clubName")
                val intro = document.getString("intro")
                val longDescription = document.getString("longDescription")
                val imageUrl = document.getString("imageUrl")

                binding.clubName.text = clubName
                binding.clubDescription.text = intro
                binding.clubLongDescription.text = longDescription
                Glide.with(this).load(imageUrl).into(binding.clubImage)
            }

        // FloatingActionButton 클릭 리스너 설정
        binding.fab.setOnClickListener {
            val action = ClubBoardFragmentDirections.actionClubBoardFragmentToIntroInputFragment(clubId)
            findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance(clubId: String) = ClubIntroFragment().apply {
            arguments = Bundle().apply {
                putString("clubId", clubId)
            }
        }
    }
}