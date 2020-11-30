//package com.android.practice.postjson.adapter
//
//import android.view.View
//import android.widget.TextView
//import com.android.practice.postjson.R
//import com.mikepenz.fastadapter.FastAdapter
//
//class PostViewHolderFast(view: View) : FastAdapter.ViewHolder<SimpleItem>(view) {
//
//    val title = itemView.findViewById<TextView>(R.id.postDetails_title_textView)
//    val body = itemView.findViewById<TextView>(R.id.postDetails_body_textView)
//
//    override fun bindView(item: SimpleItem, payloads: MutableList<Any>) {
//        title.text = item.title
//        body.text = item.body
//    }
//
//    override fun unbindView(item: SimpleItem) {
//        title.text = null
//        body.text = null
//    }
//}