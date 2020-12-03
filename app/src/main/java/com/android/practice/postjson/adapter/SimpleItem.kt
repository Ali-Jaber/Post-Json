package com.android.practice.postjson.adapter

import android.view.View
import android.widget.TextView
import com.android.practice.postjson.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

open class SimpleItem : AbstractItem<SimpleItem.ViewHolder>() {
    var userId: Int? = null
    var id: Int? = null
    var title: String? = null
    var body: String? = null

    override val type: Int
        get() = R.id.fastadapter_sample_item_id

    override val layoutRes: Int
        get() = R.layout.post_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    fun withId(id: Int): SimpleItem {
        this.id = id
        return this
    }

    fun withUserId(id: Int): SimpleItem {
        this.userId = userId
        return this
    }

    fun withTitle(name: String): SimpleItem {
        this.title = name
        return this
    }

    fun withBody(body: String): SimpleItem {
        this.body = body
        return this
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SimpleItem>(view) {
        var title: TextView = view.findViewById(R.id.postDetails_title_textView)
        var body: TextView = view.findViewById(R.id.postDetails_body_textView)

        override fun bindView(item: SimpleItem, payloads: List<Any>) {
            title.text = item.title
            body.text = item.body
        }

        override fun unbindView(item: SimpleItem) {
            title.text = null
            body.text = null
        }
    }
}