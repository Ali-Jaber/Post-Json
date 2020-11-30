package com.android.practice.postjson.adapter

import android.view.View
import android.widget.TextView
import com.android.practice.postjson.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

open class SimpleItem : AbstractItem<SimpleItem, SimpleItem.ViewHolder>() {
    var title: String? = null
    var body: String? = null

    /** defines the type defining this item. must be unique. preferably an id */
//    override val type: Int
//        get() = R.id.fastadapter_sample_item_id
//
//    /** defines the layout which will be used for this item in the list */
//    override val layoutRes: Int
//        get() = R.layout.post_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
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

    override fun getType(): Int {
        return R.id.fastadapter_sample_item_id
    }

    override fun getLayoutRes(): Int {
        return R.layout.post_item
    }
}