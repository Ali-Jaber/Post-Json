package com.android.practice.postjson.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.R

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name = itemView.findViewById<TextView>(R.id.name)
    val email = itemView.findViewById<TextView>(R.id.email)
    val body = itemView.findViewById<TextView>(R.id.body)
}