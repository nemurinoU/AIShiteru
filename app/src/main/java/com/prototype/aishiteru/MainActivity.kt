package com.prototype.aishiteru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prototype.aishiteru.databinding.ActivityMainBinding


// For location and fused location provider


class MainActivity : AppCompatActivity(){//, OnMapReadyCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fabAchievements.setOnClickListener { view ->
            Snackbar.make(view, "ACHIEVEMENTS", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabAchievements).show()

            binding.efabCheckin.visibility = View.VISIBLE
            if (navController.currentDestination?.id == R.id.ChatlistFragment) {
                navController.navigate(R.id.action_ChatlistFragment_to_AchievementFragment)
            }
            else if (navController.currentDestination?.id == R.id.ChatroomFragment) {
                navController.navigate(R.id.action_ChatroomFragment_to_AchievementFragment)
            }
            else if (navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_AchievementFragment)
            }
            else if (navController.currentDestination?.id == R.id.NewsFragment) {
                navController.navigate(R.id.action_NewsFragment_to_AchievementFragment)
            }
            else if (navController.currentDestination?.id == R.id.QuizFragment) {
                navController.navigate(R.id.action_QuizFragment_to_AchievementFragment)
            }

        }

        binding.fabChats.setOnClickListener { view ->
            Snackbar.make(view, "CHATS", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabChats).show()

            binding.efabCheckin.visibility = View.VISIBLE
            if (navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_ChatlistFragment)
            }
            else if (navController.currentDestination?.id == R.id.ChatroomFragment) {
                navController.navigate(R.id.action_ChatroomFragment_to_ChatlistFragment)
            }
            else if (navController.currentDestination?.id == R.id.NewsFragment) {
                navController.navigate(R.id.action_NewsFragment_to_ChatlistFragment)
            }
            else if (navController.currentDestination?.id == R.id.AchievementFragment) {
                navController.navigate(R.id.action_AchievementFragment_to_ChatlistFragment)
            }
            else if (navController.currentDestination?.id == R.id.QuizFragment) {
                navController.navigate(R.id.action_QuizFragment_to_ChatlistFragment)
            }

        }

        binding.fabMaps.setOnClickListener { view ->
            Snackbar.make(view, "MAPS", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabMaps).show()

            binding.efabCheckin.visibility = View.VISIBLE
            if (navController.currentDestination?.id == R.id.ChatroomFragment) {
                navController.navigate(R.id.action_ChatroomFragment_to_MapFragment)
            }
            else if (navController.currentDestination?.id == R.id.ChatlistFragment) {
                navController.navigate(R.id.action_ChatlistFragment_to_MapFragment)
            }
            else if (navController.currentDestination?.id == R.id.NewsFragment) {

                navController.navigate(R.id.action_NewsFragment_to_MapFragment)

            }
            else if (navController.currentDestination?.id == R.id.AchievementFragment) {
                navController.navigate(R.id.action_AchievementFragment_to_MapFragment)
            }
            else if (navController.currentDestination?.id == R.id.QuizFragment) {
                navController.navigate(R.id.action_QuizFragment_to_MapFragment)
            }
        }

        binding.fabNews.setOnClickListener { view ->
            Snackbar.make(view, "NEWS", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabNews).show()

            binding.efabCheckin.visibility = View.VISIBLE
            if (navController.currentDestination?.id == R.id.ChatlistFragment) {
                navController.navigate(R.id.action_ChatlistFragment_to_NewsFragment)
            }
            else if (navController.currentDestination?.id == R.id.ChatroomFragment) {
                navController.navigate(R.id.action_ChatroomFragment_to_NewsFragment)
            }
            else if (navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_NewsFragment)
            }
            else if (navController.currentDestination?.id == R.id.AchievementFragment) {
                navController.navigate(R.id.action_AchievementFragment_to_NewsFragment)
            }
            else if (navController.currentDestination?.id == R.id.QuizFragment) {
                navController.navigate(R.id.action_QuizFragment_to_NewsFragment)
            }

        }

        binding.fabQuiz.setOnClickListener { view ->
            Snackbar.make(view, "QUIZ", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabQuiz).show()

            binding.efabCheckin.visibility = View.VISIBLE
            if (navController.currentDestination?.id == R.id.ChatlistFragment) {
                navController.navigate(R.id.action_ChatlistFragment_to_QuizFragment)
            }
            else if (navController.currentDestination?.id == R.id.ChatroomFragment) {
                navController.navigate(R.id.action_ChatroomFragment_to_QuizFragment)
            }
            else if (navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_QuizFragment)
            }
            else if (navController.currentDestination?.id == R.id.AchievementFragment) {
                navController.navigate(R.id.action_AchievementFragment_to_QuizFragment)
            }
            else if (navController.currentDestination?.id == R.id.NewsFragment) {
                navController.navigate(R.id.action_NewsFragment_to_QuizFragment)
            }

        }

        binding.efabCheckin.setOnClickListener { view ->
            Snackbar.make(view, "Checked in!", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fabNews).show()

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}