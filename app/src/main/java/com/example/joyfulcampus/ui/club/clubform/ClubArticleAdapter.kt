package com.example.joyfulcampus.ui.club.clubform

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.joyfulcampus.databinding.ItemClubBinding
import com.example.joyfulcampus.ui.club.ArticleItem

class ClubArticleAdapter(
    private val onItemClicked: (ArticleItem) -> Unit,
    private val onBookmarkClicked: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ClubArticleAdapter.ViewHolder>() {

    private val items = mutableListOf<ArticleItem>()

    fun submitList(list: List<ArticleItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemClubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleItem) {
            binding.mainText.text = item.clubNameText
            binding.subText.text = item.description
            // 이미지 로드 코드 추가

            binding.root.setOnClickListener {
                onItemClicked(item)
            }

            binding.bookmarkImageButton.setOnClickListener {
                onBookmarkClicked(item.articleId, item.isBookMark)
            }
        }
    }
}