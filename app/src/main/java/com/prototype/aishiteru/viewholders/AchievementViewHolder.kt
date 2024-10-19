package com.prototype.aishiteru.viewholders

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.AchievementAdapter
import com.prototype.aishiteru.classes.Achievement

class AchievementViewHolder(itemView: View): ViewHolder(itemView) {
    private val achievementImage: ImageView = itemView.findViewById(R.id.achImage);
    private val achievementTitle: TextView = itemView.findViewById(R.id.achTitle);
    private val progressBar: ProgressBar = itemView.findViewById(R.id.achProgressBar);
    private val progressText: TextView = itemView.findViewById(R.id.achProgressText);

    fun bindAchievementData(achievement: Achievement, clickListener: AchievementAdapter.OnItemClickListener) {
        achievementImage.setImageResource(achievement.imageId);
        achievementTitle.text = achievement.title;
        progressBar.progress = achievement.currentProgress;
        progressBar.max = achievement.maxProgress;
        if(achievement.currentProgress >= achievement.maxProgress) {
            //progressText.setTextColor(Color.parseColor("#FF00FF00"));
            progressText.text = "DONE"
        }
        else progressText.text = achievement.currentProgress.toString() + " / " + achievement.maxProgress.toString();

        itemView.setOnClickListener {
            clickListener.onItemClick(achievement)
        }
    }

}