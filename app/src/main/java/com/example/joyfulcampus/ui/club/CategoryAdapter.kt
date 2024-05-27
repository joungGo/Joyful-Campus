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
    private val onCategoryClicked: (String) -> Unit // 클릭 리스너 추가
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

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

        // 카테고리 이름에 따라 다른 텍스트 색상 설정
        val textColor = when (category.name) {
            "스포츠" -> R.color.sports_color
            "의료" -> R.color.medical_color
            "봉사" -> R.color.volunteer_color
            "코딩" -> R.color.coding_color
            "창업" -> R.color.startup_color
            "종교" -> R.color.religion_color
            "예술" -> R.color.art_color
            "기타" -> R.color.things_color
            // 필요한 경우 다른 카테고리에 대한 추가 케이스를 추가하세요.
            else -> android.R.color.black // 기본 색상
        }
        holder.categoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, textColor))

        // 카테고리 클릭 이벤트 설정.
        holder.itemView.setOnClickListener {
            onCategoryClicked(category.name)
        }
    }


    override fun getItemCount() = categoryList.size
}
