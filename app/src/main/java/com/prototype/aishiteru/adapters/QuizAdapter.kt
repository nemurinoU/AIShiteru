package com.prototype.aishiteru.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.prototype.aishiteru.R
import com.prototype.aishiteru.classes.Quiz
import com.prototype.aishiteru.viewholders.QuizViewHolder

class QuizAdapter(private val data: ArrayList<Quiz>, private val itemClickListener: OnItemClickListener): Adapter<QuizViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(quiz: Quiz)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.quiz_layout, parent, false)
        // Return a new instance of our MyViewHolder passing the View object we created
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindQuizData(data.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }

}