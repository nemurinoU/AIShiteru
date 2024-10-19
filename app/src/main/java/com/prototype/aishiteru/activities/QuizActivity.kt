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
import com.prototype.aishiteru.adapters.QuizAdapter
import com.prototype.aishiteru.classes.Quiz
import com.prototype.aishiteru.helpers.DataHelper

class QuizActivity : AppCompatActivity() {

    private val quizList: ArrayList<Quiz> = DataHelper.loadQuizData()
    private lateinit var quizRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.quiz_activity_label)

        this.quizRecycler = findViewById(R.id.quizRecycler)
        this.quizRecycler.adapter = QuizAdapter(this.quizList, object : QuizAdapter.OnItemClickListener {
            override fun onItemClick(quiz: Quiz) {
                Toast.makeText(this@QuizActivity, quiz.title, Toast.LENGTH_SHORT).show()
            }
        })

        this.quizRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

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