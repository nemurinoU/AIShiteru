package com.prototype.aishiteru.viewholders

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.NewsAdapter
import com.prototype.aishiteru.classes.News

class NewsViewHolder(itemView: View): ViewHolder(itemView) {

    private val newsDifficulty: TextView = itemView.findViewById(R.id.newsDifficulty);
    private val newsHeader: TextView = itemView.findViewById(R.id.newsHeader);
    private val newsDate: TextView = itemView.findViewById(R.id.newsDate);
    private val newsImage: ImageView = itemView.findViewById(R.id.newsImage);

    fun bindNewsData(news: News, clickListener: NewsAdapter.OnItemClickListener) {

        newsImage.setImageResource(news.imageId);
        newsDifficulty.text = news.difficulty;
        newsHeader.text = news.header;
        newsDate.text = news.date.toStringISO();
        if (news.difficulty.lowercase() == "easy") {
            newsDifficulty.setBackgroundColor(Color.parseColor("#00FF00"));
        } else if (news.difficulty.lowercase() == "medium") {
            newsDifficulty.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else {
            newsDifficulty.setBackgroundColor(Color.parseColor("#FF0000"));
        }


        itemView.setOnClickListener {
            clickListener.onItemClick(news)
        }
    }
}