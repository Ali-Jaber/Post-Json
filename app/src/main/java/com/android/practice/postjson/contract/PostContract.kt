package com.android.practice.postjson.contract

import com.android.practice.postjson.model.Post


interface PostContract {
    interface View {
        fun init()

        fun showError(message: String)

        fun loadDataInList(posts: List<Post>)

    }

    interface Presenter {
        fun start()

        fun loadPosts()

        fun onViewDestroyed()
    }
}