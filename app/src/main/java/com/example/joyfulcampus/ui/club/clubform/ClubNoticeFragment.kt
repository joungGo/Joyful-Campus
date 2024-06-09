package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubNoticeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClubNoticeFragment : Fragment(R.layout.fragment_club_notice) {
    private lateinit var binding: FragmentClubNoticeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubNoticeBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        loadNoticeData(clubId)

        binding.fab.setOnClickListener {
            val action = ClubBoardFragmentDirections.actionClubBoardFragmentToNoticeInputFragment(clubId)
            findNavController().navigate(action)
        }
    }

    private fun loadNoticeData(clubId: String) {
        Firebase.firestore.collection("bulletinBoard").document(clubId).collection("notice")
            .document("noticeInfo")
            .get()
            .addOnSuccessListener { document ->
                val clubName = document.getString("clubName")
                val recruitmentPeriod = document.getString("recruitmentPeriod")
                val interview = document.getString("interview")
                val description = document.getString("description")
                val longDescription = document.getString("longDescription")
                val imageUrl = document.getString("imageUrl")

                binding.clubName.text = clubName
                binding.recruitmentPeriod.text = recruitmentPeriod
                binding.interview.text = interview
                binding.clubDescription.text = description
                binding.clubLongDescription.text = longDescription
                Glide.with(this).load(imageUrl).into(binding.clubImage)
            }
    }
}