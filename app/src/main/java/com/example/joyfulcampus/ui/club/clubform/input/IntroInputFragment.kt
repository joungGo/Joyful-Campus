package com.example.joyfulcampus.ui.club.clubform.input

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.clubBulletinBoard.IntroData
import com.example.joyfulcampus.databinding.FragmentIntroInputBinding
import com.example.joyfulcampus.viewmodel.ClubViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class IntroInputFragment : Fragment(R.layout.fragment_intro_input) {

    private lateinit var binding: FragmentIntroInputBinding
    private val viewModel: ClubViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val TAG = "IntroInputFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputClubImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.saveButton.setOnClickListener {
            val clubName = binding.inputClubName.text.toString()
            val clubDescription = binding.inputClubDescription.text.toString()
            val clubLongDescription = binding.inputClubLongDescription.text.toString()

            if (selectedImageUri != null) {
                uploadImageToFirebaseStorage(selectedImageUri!!) { imageUrl ->
                    val introData = IntroData(clubName, clubDescription, clubLongDescription, imageUrl)
                    saveIntroDataToFirestore(introData)
                    viewModel.saveIntroData(introData)  // ViewModel에 데이터 저장
                }
            } else {
                val introData = IntroData(clubName, clubDescription, clubLongDescription, "")
                saveIntroDataToFirestore(introData)
                viewModel.saveIntroData(introData)  // ViewModel에 데이터 저장
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            selectedImageUri = data.data
            binding.inputClubImage.setImageURI(selectedImageUri)
        } else {
            Toast.makeText(requireContext(), "이미지 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("bulletinBoard/photo/$fileName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Image upload failed", exception)
                Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveIntroDataToFirestore(introData: IntroData) {
        val clubId = UUID.randomUUID().toString()
        firestore.collection("bulletinBoard").document(clubId)
            .set(introData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error writing document", exception)
                Toast.makeText(requireContext(), "데이터 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
