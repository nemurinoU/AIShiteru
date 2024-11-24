package com.prototype.aishiteru.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.prototype.aishiteru.R
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.viewholders.ChatlistViewHolder
import com.prototype.aishiteru.viewholders.MessagesViewHolder

class ChatlistAdapter(private val castList: ArrayList<CastItem>, private val itemClickListener: OnItemClickListener): Adapter<ChatlistViewHolder>() {
    /*  onCreateViewHolder requires in two parameters:
            parent -> Which is the parent View where this adapter is associated with; this is
                      typically the RecyclerView
                      recall: recyclerView.adapter = MyAdapter(this.characterList)
            viewType -> This parameter refers to the
    * */

    interface OnItemClickListener {
        fun onItemClick(castItem: CastItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatlistViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.cast_item_layout, parent, false)
        // Return a new instance of our MyViewHolder passing the View object we created
        return ChatlistViewHolder(view)
    }

    /*  Whenever the RecyclerView feels the need to bind data, onBindViewHolder is called. Here, we
        gain access to the specific ViewHolder instance and the position in our data that we should
        be binding to the view.
    * */
    override fun onBindViewHolder(holder: ChatlistViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindCastData(castList.get(position), itemClickListener)
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return castList.size
    }
}