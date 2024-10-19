package com.prototype.aishiteru.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        setSupportActionBar(findViewById(R.id.toolbar3))
        supportActionBar?.title = getString(R.string.news_activity_label)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}