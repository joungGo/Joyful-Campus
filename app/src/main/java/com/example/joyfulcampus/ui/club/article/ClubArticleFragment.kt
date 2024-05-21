package com.example.joyfulcampus.ui.club.article

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.FragmentReadClubBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

// 동아리 눌렀을 때 다음 화면으로 넘어가는 것 구현하는 Fragment
class ClubArticleFragment : Fragment(R.layout.fragment_read_club) {
    private lateinit var binding: FragmentReadClubBinding
    private val args: ClubArticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReadClubBinding.bind(view)

        val articleId = args.articleId

        binding.toolbar.setupWithNavController(findNavController())

        Firebase.firestore.collection("articles").document(articleId)
            .get()
            .addOnSuccessListener {
                val model = it.toObject<ArticleModel>()
                binding.mainText.text = model?.clubNameText
                binding.subText.text = model?.description

                Glide.with(binding.topSector)
                    .load(model?.imageUrl)
                    .into(binding.topSector)
            }.addOnFailureListener {

            }
    }
}