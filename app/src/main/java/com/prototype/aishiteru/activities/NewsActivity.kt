package com.prototype.aishiteru.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.NewsAdapter
import com.prototype.aishiteru.classes.News
import com.prototype.aishiteru.helpers.DataHelper

class NewsActivity : AppCompatActivity() {

    private val newsList: ArrayList<News> = DataHelper.loadNewsData()
    private lateinit var newsRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)

        this.newsRecycler = findViewById(R.id.newsRecycler)
        this.newsRecycler.adapter = NewsAdapter(this.newsList, object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(news: News) {
                Toast.makeText(this@NewsActivity, news.content, Toast.LENGTH_SHORT).show()
            }
        })

        this.newsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}