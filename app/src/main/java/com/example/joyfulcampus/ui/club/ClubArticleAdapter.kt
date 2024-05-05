package com.example.joyfulcampus.ui.club

import androidx.recyclerview.widget.RecyclerView
import com.example.joyfulcampus.databinding.ItemClubBinding
import com.example.joyfulcampus.ui.club.article.ArticleItem

class ClubArticleAdapter  {
    inner class ViewHolder(private val binding: ItemClubBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(articleItem: ArticleItem) {

            }
        }
}