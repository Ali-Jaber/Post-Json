package com.android.practice.postjson.db

import androidx.room.Query
import com.android.practice.postjson.model.Post
import io.reactivex.rxjava3.core.Single

interface PostDao {

    @Query("select * from post_table")
    fun getPosts(): Single<List<Post>>
}