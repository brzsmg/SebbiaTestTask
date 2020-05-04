package com.sebbia.brzsmg.testtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sebbia.brzsmg.testtask.R
import com.sebbia.brzsmg.testtask.models.News

/**
 * Адаптер для новостей.
 */
class NewsAdapter(
    private var data: List<News>,
    private val onNewsSelected: (news: News) -> Unit) : RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.mvDate.text = data[position].date.toFormat("dd.MM.yyyy hh:mm")
        holder.mvTitle.text = data[position].title
        holder.mvShortDescription.text = data[position].shortDescription
        holder.itemView.setOnClickListener {
            onNewsSelected.invoke(data[position])
        }
    }
}

class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mvDate : TextView = itemView.findViewById(R.id.date)
    var mvTitle : TextView = itemView.findViewById(R.id.title)
    var mvShortDescription : TextView = itemView.findViewById(R.id.short_description)
}