package com.example.joyfulcampus.ui.club.clubform.input

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentNoticeInputBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class NoticeInputFragment : Fragment(R.layout.fragment_notice_input) {
    private lateinit var binding: FragmentNoticeInputBinding
    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(binding.inputClubImage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoticeInputBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        loadNoticeData(clubId)

        binding.inputClubImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            val clubName = binding.inputClubName.text.toString()
            val recruitmentPeriod = binding.inputRecruitmentPeriod.text.toString()
            val interview = binding.inputInterview.text.toString()
            val description = binding.inputClubDescription.text.toString()
            val longDescription = binding.inputClubLongDescription.text.toString()

            if (selectedImageUri != null) {
                uploadImageAndSaveData(clubId, clubName, recruitmentPeriod, interview, description, longDescription, selectedImageUri!!)
            } else {
                saveData(clubId, clubName, recruitmentPeriod, interview, description, longDescription, currentImageUrl)
            }
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
                currentImageUrl = document.getString("imageUrl")

                binding.inputClubName.setText(clubName)
                binding.inputRecruitmentPeriod.setText(recruitmentPeriod)
                binding.inputInterview.setText(interview)
                binding.inputClubDescription.setText(description)
                binding.inputClubLongDescription.setText(longDescription)
                currentImageUrl?.let {
                    Glide.with(this).load(it).into(binding.inputClubImage)
                }
            }
    }

    private fun uploadImageAndSaveData(clubId: String, clubName: String, recruitmentPeriod: String, interview: String, description: String, longDescription: String, imageUri: Uri) {
        val storageRef = Firebase.storage.reference.child("club_notice_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("NoticeInputFragment", "Download URL: $uri")
                    saveData(clubId, clubName, recruitmentPeriod, interview, description, longDescription, uri.toString())
                }
            }
            .addOnFailureListener {exception ->
                Log.e("NoticeInputFragment", "Image upload failed", exception)
                // 실패 시 처리
            }
    }

    private fun saveData(clubId: String, clubName: String, recruitmentPeriod: String, interview: String, description: String, longDescription: String, imageUrl: String?) {
        val data = hashMapOf(
            "clubName" to clubName,
            "recruitmentPeriod" to recruitmentPeriod,
            "interview" to interview,
            "description" to description,
            "longDescription" to longDescription,
            "imageUrl" to imageUrl
        )

        Firebase.firestore.collection("bulletinBoard").document(clubId).collection("notice")
            .document("noticeInfo")
            .set(data)
            .addOnSuccessListener {
                Log.d("IntroInputFragment", "Data saved successfully")
                findNavController().navigateUp()
            }
            .addOnFailureListener {exception ->
                Log.e("IntroInputFragment", "Data save failed", exception)
                // 실패 시 처리
            }
    }
}
