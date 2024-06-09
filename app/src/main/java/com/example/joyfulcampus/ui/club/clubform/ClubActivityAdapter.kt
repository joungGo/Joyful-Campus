package com.example.joyfulcampus.ui.club.clubform

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class ClubActivityAdapter : ListAdapter<Activity, ActivityViewHolder>(ActivityDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
    }
}
