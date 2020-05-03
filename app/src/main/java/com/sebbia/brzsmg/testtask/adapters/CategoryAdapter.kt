package com.sebbia.brzsmg.testtask.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.model.Category

/**
 * Адаптер для категорий.
 */
class CategoryAdapter(
    private var data: List<Category>,
    private val onCategorySelected: (category: Category) -> Unit) : RecyclerView.Adapter<CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.mvName.text = data[position].name
        holder.itemView.setOnClickListener {
            onCategorySelected.invoke(data[position])
        }
    }
}

class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mvName : TextView = itemView.findViewById(R.id.name)
}