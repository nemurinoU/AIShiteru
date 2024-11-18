package com.prototype.aishiteru.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.QuizAdapter
import com.prototype.aishiteru.classes.Quiz

class MessagesViewHolder(itemView:View):ViewHolder(itemView) {
    private val user: TextView = itemView.findViewBy
}
class QuizViewHolder(itemView:View):ViewHolder(itemView) {

    private val quizTitle: TextView = itemView.findViewById(R.id.quizTitle);
    private val quizHiScore: TextView = itemView.findViewById(R.id.quizHiScore);
    private val quizImage: ImageView = itemView.findViewById(R.id.quizImage);

    fun bindQuizData(quiz: Quiz, clickListener: QuizAdapter.OnItemClickListener) {

        quizImage.setImageResource(quiz.imageId);
        quizTitle.text = quiz.title;
        quizHiScore.text = quiz.hiScore.toString()+" / 10";

        itemView.setOnClickListener {
            clickListener.onItemClick(quiz)
        }
    }
















}