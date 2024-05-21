package com.example.joyfulcampus.ui.club

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.data.ArticleModel
import com.example.joyfulcampus.databinding.ItemClubBinding

class ClubArticleAdapter(val onItemClicked: (ArticleModel) -> Unit): ListAdapter<ArticleModel, ClubArticleAdapter.ViewHolder> (diffUtil) {
    inner class ViewHolder(private val binding: ItemClubBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(articleModel: ArticleModel) {
                binding.mainText.text = articleModel.clubNameText
                binding.subText.text = articleModel.description

                Glide.with(binding.topSector)
                    .load(articleModel.imageUrl) // 받아와서
                    .into(binding.topSector) // 넣기

                // 클릭된 item에 대한 클릭 이벤트 설정 -> TODO: item을 클릭하면 새로운 화면으로 이동 구현
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