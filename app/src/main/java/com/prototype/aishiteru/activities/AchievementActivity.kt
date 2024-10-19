package com.prototype.aishiteru.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.title = getString(R.string.achievements_activity_label)
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