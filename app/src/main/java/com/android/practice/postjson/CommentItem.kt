package com.android.practice.postjson

import com.android.practice.postjson.model.Comments
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.comments_item.view.*

class CommentItem(private val comments: Comments) :
    Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name.text = comments.name
        viewHolder.itemView.email.text = comments.email
        viewHolder.itemView.body.text = comments.body
    }

    override fun getLayout(): Int = R.layout.comments_item
}