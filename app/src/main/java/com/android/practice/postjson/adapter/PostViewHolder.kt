package com.android.practice.postjson.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.R
//import com.android.practice.postjson.databinding.PostItemBinding
import com.android.practice.postjson.model.Post

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById<TextView>(R.id.postDetails_title_textView)
    val body = itemView.findViewById<TextView>(R.id.postDetails_body_textView)
}