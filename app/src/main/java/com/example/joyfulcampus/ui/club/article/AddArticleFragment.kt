package com.example.joyfulcampus.ui.club.article

import android.database.DatabaseErrorHandler
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.FragmentAddClubBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddArticleFragment : Fragment(R.layout.fragment_add_club) {
    private lateinit var binding: FragmentAddClubBinding

    private var selectedUri: Uri? = null // 이미지가 선택되었을 때 임시로 저장되는 변수

    // 로컬(휴대폰)에서 이미지를 선택할 수 있는 기능 구현
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                selectedUri = uri
                binding.photoImageView.setImageURI(uri)
                binding.plusButton.isVisible = false
                binding.deleteButton.isVisible = true
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddClubBinding.bind(view)

        startPicker()
        setupPhotoImageView()

        setupDeleteButton()

        setupSubmitButton(view)

        setupBackButton()

    }

    private fun setupDeleteButton() {
        binding.deleteButton.setOnClickListener {
            binding.photoImageView.setImageURI(null)
            selectedUri = null
            binding.deleteButton.isVisible = false
            binding.plusButton.isVisible = true
        }
    }

    private fun setupPhotoImageView() {
        binding.photoImageView.setOnClickListener {
            if (selectedUri == null) {
                startPicker()
            }
        }
    }

    private fun setupSubmitButton(view: View) {
        binding.submitButton.setOnClickListener {
            showProgress()

            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadImage(
                    uri = photoUri,
                    successHandler = {photoUrl ->
                        // Firestore 데이터 업로드
                        uploadArticle(
                            photoUrl,
                            binding.descriptionMainEditText.text.toString(),
                            binding.descriptionSubEditText.text.toString()
                        )
                    },
                    errorHandler = {
                        Snackbar.make(view, "이미지 업로드에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                        hideProgress()
                    })

            } else {
                Snackbar.make(view, "이미지가 선탤되지 않았습니다.", Snackbar.LENGTH_SHORT).show()
                hideProgress()
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(AddArticleFragmentDirections.actionBack())
        }
    }

    private fun startPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showProgress() {
        binding.progressBarLayout.isVisible = true

    }

    private fun hideProgress() {
        binding.progressBarLayout.isVisible = false
    }

    private fun uploadImage(
        uri: Uri,
        successHandler: (String) -> Unit,
        errorHandler: (Throwable?) -> Unit
    ) {
        val fileName = "${UUID.randomUUID()}.png"
        Firebase.storage.reference.child("articles/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Firebase.storage.reference.child("articles/photo/$fileName")
                        .downloadUrl
                        .addOnSuccessListener {
                            successHandler(it.toString())
                        }.addOnFailureListener {
                            errorHandler(it)
                        }
                } else {
                    errorHandler(task.exception)
                }
            }
    }

    private fun uploadArticle(photoUrl: String, clubName: String ,description: String) {
        val articleId = UUID.randomUUID().toString()
        val articleModel = ArticleModel(
            articleId = articleId,
            createdAt = System.currentTimeMillis(),
            clubNameText = clubName,
            description = description,
            imageUrl = photoUrl,
        )

        Firebase.firestore.collection("articles").document(articleId)
            .set(articleModel)
            .addOnSuccessListener {
                findNavController().navigate(AddArticleFragmentDirections.actionAddArticleFragmentToClubFragment())
                hideProgress()
            }.addOnFailureListener {
                it.printStackTrace()
                view?.let {view ->
                    Snackbar.make(view, "글 작성에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                }
                hideProgress()
            }

        hideProgress()
    }
}