package com.example.joyfulcampus.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.ItemClubBinding

class BookmarkArticleAdapter(val onItemClicked: (ArticleModel) -> Unit) : ListAdapter<ArticleModel, BookmarkArticleAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemClubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {
            binding.mainText.text = articleModel.clubNameText
            binding.subText.text = articleModel.description

            binding.bookmarkImageButton.isVisible = false

            Glide.with(binding.topSector)
                .load(articleModel.imageUrl)
                .into(binding.topSector)

            binding.root.setOnClickListener {
                onItemClicked(articleModel)
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
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.articleId == newItem.articleId
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
