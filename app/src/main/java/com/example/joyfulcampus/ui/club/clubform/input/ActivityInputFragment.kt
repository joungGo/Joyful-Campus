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
import com.example.joyfulcampus.databinding.FragmentActivityInputBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class ActivityInputFragment : Fragment(R.layout.fragment_activity_input) {
    private lateinit var binding: FragmentActivityInputBinding
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(binding.inputActivityImage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActivityInputBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        binding.inputActivityImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageAndSaveData(clubId, selectedImageUri!!)
            }
        }
    }

    private fun uploadImageAndSaveData(clubId: String, imageUri: Uri) {
        val storageRef = Firebase.storage.reference.child("club_activity_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveData(clubId, uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ActivityInputFragment", "Image upload failed", exception)
            }
    }

    private fun saveData(clubId: String, imageUrl: String) {
        val data = hashMapOf(
            "imageUrl" to imageUrl,
            "timestamp" to System.currentTimeMillis()
        )

        Firebase.firestore.collection("bulletinBoard").document(clubId).collection("activities")
            .add(data)
            .addOnSuccessListener {
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Log.e("ActivityInputFragment", "Data save failed", exception)
            }
    }
}
