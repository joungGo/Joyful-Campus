package com.example.joyfulcampus.ui.club

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.joyfulcampus.R

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onCategoryClicked: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedCategory: String? = null

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon = itemView.findViewById<ImageView>(R.id.categoryIcon)
        val categoryName = itemView.findViewById<TextView>(R.id.categoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.catergory_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryIcon.setImageResource(category.iconResourceId)
        holder.categoryName.text = category.name

        val textColor = when (category.name) {
            "스포츠" -> R.color.sports_color
            "의료" -> R.color.medical_color
            "봉사" -> R.color.volunteer_color
            "코딩" -> R.color.coding_color
            "창업" -> R.color.startup_color
            "종교" -> R.color.religion_color
            "예술" -> R.color.art_color
            "기타" -> R.color.things_color
            else -> android.R.color.black
        }
        holder.categoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, textColor))

        // 선택된 카테고리에 따라 투명도 조절
        holder.itemView.alpha = if (selectedCategory == null || selectedCategory == category.name) 1.0f else 0.25gif

        holder.itemView.setOnClickListener {
            if (selectedCategory == category.name) {
                selectedCategory = null
            } else {
                selectedCategory = category.name
            }
            notifyDataSetChanged() // RecyclerView 갱신
            onCategoryClicked(category.name)
        }
    }

    override fun getItemCount() = categoryList.size
}
