package com.prototype.aishiteru.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.prototype.aishiteru.R
import com.prototype.aishiteru.classes.News
import com.prototype.aishiteru.viewholders.NewsViewHolder


class NewsAdapter(private val data: ArrayList<News>, private val itemClickListener: OnItemClickListener): Adapter<NewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(news: News)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.news_layout, parent, false)
        // Return a new instance of our MyViewHolder passing the View object we created
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindNewsData(data.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }


}