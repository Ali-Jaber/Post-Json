package com.android.practice.postjson.services.jsonplaceholder

import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.model.User
import io.reactivex.rxjava3.core.Observable

interface PostService {

    fun list(): Observable<List<Post>>
    fun get(postId: Int): Observable<Post>
    fun getPostComments(postId:Int): Observable<List<Comments>>
    fun getUserById(userId: Int): Observable<User>
    fun create(post: Post): Observable<Post>
    fun deleteById(postId: Int): Observable<Post>
}