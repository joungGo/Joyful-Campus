package com.example.joyfulcampus.ui.club.clubform

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joyfulcampus.databinding.ItemActivityBinding

class ActivityViewHolder(private val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(activity: Activity) {
        Glide.with(binding.root.context).load(activity.imageUrl).into(binding.activityImage)
    }

    companion object {
        fun from(parent: ViewGroup): ActivityViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemActivityBinding.inflate(layoutInflater, parent, false)
            return ActivityViewHolder(binding)
        }
    }
}
