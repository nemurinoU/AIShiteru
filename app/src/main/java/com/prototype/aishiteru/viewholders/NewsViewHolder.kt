package com.prototype.aishiteru.viewholders

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.NewsAdapter
import com.prototype.aishiteru.classes.News

import com.bumptech.glide.Glide

class NewsViewHolder(itemView: View): ViewHolder(itemView) {

    private val newsDifficulty: TextView = itemView.findViewById(R.id.newsDifficulty)
    private val newsHeader: TextView = itemView.findViewById(R.id.newsHeader)
    private val newsDate: TextView = itemView.findViewById(R.id.newsDate)
    private val newsImage: ImageView = itemView.findViewById(R.id.newsImage)

    fun bindNewsData(news: News, clickListener: NewsAdapter.OnItemClickListener) {

        // Use Glide to load the image from the URL into newsImage
        Glide.with(itemView.context)
            .load(news.imageUrl) // Load the image URL
            .placeholder(R.drawable.placeholder_image) // Show a placeholder while loading
            .error(R.drawable.placeholder_image) // Show an error image if loading fails
            .into(newsImage) // Set the ImageView to display the image

        newsDifficulty.text = news.difficulty
        newsHeader.text = news.header
        newsDate.text = news.date.toStringISO()

        // Set background color based on difficulty
        when (news.difficulty.lowercase()) {
            "breaking" -> newsDifficulty.setBackgroundResource(R.drawable.soft_tag).also {
                newsDifficulty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF6B6B"))
            }
            "society" -> newsDifficulty.setBackgroundResource(R.drawable.soft_tag).also {
                newsDifficulty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f5bb1b"))
            }
            "daily life" -> newsDifficulty.setBackgroundResource(R.drawable.soft_tag).also {
                newsDifficulty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#90ce2e"))
            }
            "science & culture" -> newsDifficulty.setBackgroundResource(R.drawable.soft_tag).also {
                newsDifficulty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFB347"))
            }
            else -> newsDifficulty.setBackgroundResource(R.drawable.soft_tag).also {
                newsDifficulty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#73A9E1"))
            }
        }

        itemView.setOnClickListener {
            clickListener.onItemClick(news)
        }
    }
}