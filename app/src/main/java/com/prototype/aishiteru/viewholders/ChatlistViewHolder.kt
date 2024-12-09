package com.prototype.aishiteru.viewholders

import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.MessageItem
import android.text.Spannable
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prototype.aishiteru.adapters.AchievementAdapter
import com.prototype.aishiteru.adapters.ChatlistAdapter
import com.prototype.aishiteru.classes.CastItem
import de.hdodenhof.circleimageview.CircleImageView

class ChatlistViewHolder(itemView:View):ViewHolder(itemView) {
    private val av: CircleImageView = itemView.findViewById(R.id.avaPreview)
    private val name: TextView = itemView.findViewById(R.id.namPreview)

    // bind the data of each message in a chatroom accordingly
    // we need to get the properties of the constructor
    // let's break this down
    fun bindCastData(data: CastItem, clickListener: ChatlistAdapter.OnItemClickListener) {
        //viewHolder.textView_message_text.text = data.text
        av.setImageResource(data.imageId) // set avatar of cast member in the list
        name.text = data.name

        itemView.setOnClickListener {
            clickListener.onItemClick(data)
        }
    }
}