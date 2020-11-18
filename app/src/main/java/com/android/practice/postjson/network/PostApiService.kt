package com.android.practice.postjson.network

import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.model.Post
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApiService {

    @GET("posts")
    fun getPosts(): Observable<List<Post>>

    @GET("posts/{postId}")
    fun getPostDetails(@Path("postId") postId: Int): Observable<Post>

    @GET("posts/{postId}/comments")
    fun getCommentsDetails(@Path("postId") postId: Int): Observable<List<Comments>>
}