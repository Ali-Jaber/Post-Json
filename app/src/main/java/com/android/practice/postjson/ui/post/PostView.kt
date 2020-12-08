package com.android.practice.postjson.ui.post

import com.android.practice.postjson.base.BaseView
import com.android.practice.postjson.model.Post

interface PostView: BaseView {

    fun updatePosts(posts: List<Post>)
}