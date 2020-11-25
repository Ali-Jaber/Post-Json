package com.android.practice.postjson.network

import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.model.Post
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface PostApiService {

    @GET("posts")
    fun getPosts(): Observable<List<Post>>

    @GET("posts/{postId}")
    fun getPostDetails(@Path("postId") postId: Int): Observable<Post>

    @GET("posts/{postId}/comments")
    fun getCommentsDetails(@Path("postId") postId: Int): Observable<List<Comments>>

    @POST("posts")
    fun createPost(@Body postModel: Post): Observable<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int?): Observable<Post>
}