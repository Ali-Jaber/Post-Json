package com.android.practice.postjson.services.jsonplaceholder

import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.model.User
import com.android.practice.postjson.network.PostApiService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PostServiceRemoteImpl @Inject constructor(
    private val api: PostApiService
) : PostService {
    override fun list(): Observable<List<Post>> = api.getPosts()


    override fun get(postId: Int): Observable<Post> = api.getPostDetails(postId)

    override fun getPostComments(postId: Int): Observable<List<Comments>> =
        api.getCommentsDetails(postId)

    override fun getUserById(userId: Int): Observable<User> = api.getUser(userId)

    override fun create(post: Post): Observable<Post> = api.createPost(post)

    override fun deleteById(postId: Int): Observable<Post> = api.deletePost(postId)

}