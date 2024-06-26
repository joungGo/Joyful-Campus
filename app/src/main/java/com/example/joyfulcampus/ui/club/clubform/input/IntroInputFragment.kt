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
import com.example.joyfulcampus.databinding.FragmentIntroInputBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class IntroInputFragment : Fragment(R.layout.fragment_intro_input) {
    private lateinit var binding: FragmentIntroInputBinding
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
        binding = FragmentIntroInputBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        loadClubData(clubId)

        binding.inputClubImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            val clubName = binding.inputClubName.text.toString()
            val clubDescription = binding.inputClubDescription.text.toString()
            val clubLongDescription = binding.inputClubLongDescription.text.toString()

            if (selectedImageUri != null) {
                uploadImageAndSaveData(clubId, clubName, clubDescription, clubLongDescription, selectedImageUri!!)
            } else {
                saveData(clubId, clubName, clubDescription, clubLongDescription, currentImageUrl)
            }
        }
    }

    private fun loadClubData(clubId: String) {
        Firebase.firestore.collection("bulletinBoard").document(clubId)
            .get()
            .addOnSuccessListener { document ->
                val clubName = document.getString("clubName")
                val intro = document.getString("intro")
                val longDescription = document.getString("longDescription")
                currentImageUrl = document.getString("imageUrl")

                binding.inputClubName.setText(clubName)
                binding.inputClubDescription.setText(intro)
                binding.inputClubLongDescription.setText(longDescription)
                currentImageUrl?.let {
                    Glide.with(this).load(it).into(binding.inputClubImage)
                }
            }
    }

    private fun uploadImageAndSaveData(clubId: String, clubName: String, clubDescription: String, clubLongDescription: String, imageUri: Uri) {
        val storageRef = Firebase.storage.reference.child("club_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("IntroInputFragment", "Download URL: $uri")
                    saveData(clubId, clubName, clubDescription, clubLongDescription, uri.toString())
                }
            }
            .addOnFailureListener {exception ->
                Log.e("IntroInputFragment", "Image upload failed", exception)
                // 실패 시 처리
            }
    }

    private fun saveData(clubId: String, clubName: String, clubDescription: String, clubLongDescription: String, imageUrl: String?) {
        val data = hashMapOf(
            "clubName" to clubName,
            "intro" to clubDescription,
            "longDescription" to clubLongDescription,
            "imageUrl" to imageUrl
        )

        Firebase.firestore.collection("bulletinBoard").document(clubId)
            .set(data)
            .addOnSuccessListener {
                // 성공 시 처리
                Log.d("NoticeInputFragment", "Data saved successfully")
                findNavController().navigateUp()
            }
            .addOnFailureListener {exception ->
                Log.e("NoticeInputFragment", "Data save failed", exception)
                // 실패 시 처리
            }
    }
}
