package com.example.joyfulcampus.ui.club

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: List<Category>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubBinding.bind(view)

        // 툴바 설정
        val toolbar = binding.clubToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true) // 프래그먼트에서 옵션 메뉴를 처리하기 위해 설정

        setupToolbarTitle()

        setupAddButton(view)

        val articleAdapter = ClubArticleAdapter {
            it.articleId
            findNavController().navigate(
                ClubFragmentDirections.actionClubFragmentToClubArticleFragment(
                    articleId = it.articleId.orEmpty()
                )
            )
        }

        binding.clubRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = articleAdapter
        }

        // 여기 선언하고 ClubArticleFragment로 넘어감
        Firebase.firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map {
                    it.toObject<ArticleModel>()
                }

                articleAdapter.submitList(list)
            }

        //------------------------------------------------------------------------------

        categoryRecyclerView = binding.categoryRecyclerView
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 카테고리 데이터 추가
        categoryList = listOf(
            Category(R.drawable.sport, "스포츠"),
            Category(R.drawable.medical, "의료"),
            Category(R.drawable.volunteer, "봉사"),
            Category(R.drawable.coding, "코딩"),
            Category(R.drawable.startup, "창업"),
            Category(R.drawable.religion, "종교"),
            Category(R.drawable.art, "예술"),
            Category(R.drawable.things, "기타")
        )

        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter

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

    private fun setupToolbarTitle() {
        val toolbar = binding.clubToolbar

        val fullTitle = "편안한 Campus"
        val spannableString = SpannableString(fullTitle)

        val startIndex = fullTitle.indexOf("편안한")
        val endIndex = startIndex + "편안한".length

        if (startIndex != -1) {
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.편안한)),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        toolbar.title = spannableString
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
