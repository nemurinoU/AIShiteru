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
}