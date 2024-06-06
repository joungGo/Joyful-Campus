package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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

        Firebase.firestore.collection("clubs").document(clubId)
            .get()
            .addOnSuccessListener { document ->
                val clubName = document.getString("name")
                val clubDescription = document.getString("description")
                val clubImage = document.getString("imageUrl")

                binding.clubName.text = clubName
                binding.clubDescription.text = clubDescription
                // Load the image using Glide or any other image loading library
                // Glide.with(this).load(clubImage).into(binding.clubImage)
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
