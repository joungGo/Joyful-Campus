package com.example.joyfulcampus.ui.club

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.FragmentClubBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ClubFragment : Fragment(R.layout.fragment_club) {
    private lateinit var binding: FragmentClubBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubBinding.bind(view)

        // 툴바 설정
        val toolbar = binding.clubToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true) // 프래그먼트에서 옵션 메뉴를 처리하기 위해 설정

        setupAddButton(view)

        val articleAdapter = ClubArticleAdapter {

        }

        binding.clubRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = articleAdapter
        }

        Firebase.firestore.collection("articles")
            .get()
            .addOnSuccessListener {result ->
                val list = result.map {
                    it.toObject<ArticleModel>()
                }

                articleAdapter.submitList(list)
            }
    }

    private fun setupAddButton(view: View) {
        binding.addButton.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                val action = ClubFragmentDirections.actionClubFragmentToAddArticleFragment()
                findNavController().navigate(action)
            } else {
                Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.menu_club_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.alarm -> {
                // 알람 아이템 클릭 시 처리할 코드
                return true
            }
            R.id.find -> {
                // 찾기 아이템 클릭 시 처리할 코드
                return true
            }
            R.id.bookmark -> {
                // 북마크 아이템 클릭 시 처리할 코드
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
