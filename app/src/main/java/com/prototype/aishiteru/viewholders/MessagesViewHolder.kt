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
import com.prototype.aishiteru.classes.CastItem
import de.hdodenhof.circleimageview.CircleImageView

class MessagesViewHolder(itemView:View):ViewHolder(itemView) {
    private val av: CircleImageView = itemView.findViewById(R.id.avChatter)
    private val time: TextView = itemView.findViewById(R.id.msgTimestamp)
    private val body: TextView = itemView.findViewById(R.id.msgBody)
    private val row: TableRow = itemView.findViewById(R.id.tblAlign)

    // bind the data of each message in a chatroom accordingly
    // we need to get the properties of the constructor
    // let's break this down
    fun bindMessageData(data: MessageItem, recipient: String) {
        //viewHolder.textView_message_text.text = data.text


        // later make it so that the condition changes with the chatroom opened
        if (data.sender.userId.startsWith("0x")) {
            time.setText(data.time.toStringISO(true)) // time stamp
            body.setText(data.text)

            // if the message is from a bot
            // let's dissect this
            av.setImageResource(data.avatar)   // bot profile photo

            // the funny part
            // we gotta set the properties of the chatter box
            // then we gotta set the color and the shape(?)
            body.setBackgroundResource(R.drawable.rect_corner_oval_dark)
            row.setHorizontalGravity(Gravity.LEFT)
        }
        else {
            time.setText(data.time.toStringISO(true)) // time stamp
            body.setText(data.text)
            // if the message is from the user
            //av.setImageResource(data.imageId) // no profile photo needed
            av.visibility = View.GONE
            body.setBackgroundResource(R.drawable.rect_corner_oval_user) // set it to blue chatbox
            row.setHorizontalGravity(Gravity.RIGHT) // move to the right
            time.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        }
    }
}