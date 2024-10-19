package com.prototype.aishiteru.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.AchievementAdapter
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.helpers.DataHelper

class AchievementActivity : AppCompatActivity() {

    private val achievementList: ArrayList<Achievement> = DataHelper.loadAchievementData()
    private lateinit var achievementRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_achievement)

        this.achievementRecycler = findViewById(R.id.achRecycler)
        this.achievementRecycler.adapter = AchievementAdapter(this.achievementList, object : AchievementAdapter.OnItemClickListener {
            override fun onItemClick(achievement: Achievement) {
                Toast.makeText(this@AchievementActivity, achievement.description, Toast.LENGTH_SHORT).show()
            }
        })

        this.achievementRecycler.layoutManager = GridLayoutManager(this, 4)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}