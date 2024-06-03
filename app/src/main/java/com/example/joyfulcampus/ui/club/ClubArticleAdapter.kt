package com.example.joyfulcampus.ui.club

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.ItemClubBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClubArticleAdapter(
    val onItemClicked: (ArticleItem) -> Unit,
    val onBookmarkClicked: (String, Boolean) -> Unit
) : ListAdapter<ArticleItem, ClubArticleAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemClubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleItem: ArticleItem) {
            binding.mainText.text = articleItem.clubNameText
            binding.subText.text = articleItem.description

            Glide.with(binding.topSector)
                .load(articleItem.imageUrl)
                .into(binding.topSector)

            binding.root.setOnClickListener {
                onItemClicked(articleItem)
            }

            if (articleItem.isBookMark) {
                binding.bookmarkImageButton.setBackgroundResource(R.drawable.bookmark_icon_club_black)
            } else {
                binding.bookmarkImageButton.setBackgroundResource(R.drawable.bookmark_icon_club)
            }

            binding.bookmarkImageButton.setOnClickListener {
                onBookmarkClicked.invoke(articleItem.articleId, articleItem.isBookMark.not())

                articleItem.isBookMark = articleItem.isBookMark.not()
                if (articleItem.isBookMark) {
                    binding.bookmarkImageButton.setBackgroundResource(R.drawable.bookmark_icon_club_black)
                } else {
                    binding.bookmarkImageButton.setBackgroundResource(R.drawable.bookmark_icon_club)
                }

                // Firebase에 북마크 상태 업데이트
                val uid = Firebase.auth.currentUser?.uid.orEmpty()
                val bookmarkRef = Firebase.firestore.collection("bookmark").document(uid)

                bookmarkRef.get().addOnSuccessListener { document ->
                    val articleIds = document.get("articleIds") as? MutableList<String> ?: mutableListOf()
                    Log.d("Bookmark", "Current articleIds: $articleIds")

                    if (articleItem.isBookMark) {
                        if (!articleIds.contains(articleItem.articleId)) {
                            articleIds.add(articleItem.articleId)
                            Log.d("Bookmark", "Added articleId: ${articleItem.articleId}")
                        }
                    } else {
                        articleIds.remove(articleItem.articleId)
                        Log.d("Bookmark", "Removed articleId: ${articleItem.articleId}")
                    }

                    bookmarkRef.set(mapOf("articleIds" to articleIds)).addOnSuccessListener {
                        Log.d("Bookmark", "Bookmark updated successfully")
                    }.addOnFailureListener {
                        Log.e("Bookmark", "Failed to update bookmark", it)
                    }
                }.addOnFailureListener {
                    Log.e("Bookmark", "Failed to get bookmark document", it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemClubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem.articleId == newItem.articleId
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}