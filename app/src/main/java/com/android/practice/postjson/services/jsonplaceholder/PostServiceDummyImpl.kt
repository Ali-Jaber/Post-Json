package com.android.practice.postjson.services.jsonplaceholder

import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.model.User
import com.android.practice.postjson.network.PostApiService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PostServiceDummyImpl: PostService {

    private val posts = initPosts()

    private fun initPosts() {
        listOf(
            Post(1, 1, "First Title", "First Body"),
            Post(2, 2, "Second Title", "Second Body")
        )
    }

    private val comments = initComments()

    private fun initComments() {
        ArrayList<Comments>().apply {
            add(Comments(1, 1, "First Title", "test@gmail.com", "First Comment"))
            add(Comments(2, 2, "Second Title", "test@hotmail.com", "Second Comment"))
        }
    }

    override fun list(): Observable<List<Post>> = Observable.just(
        listOf(
            Post(1, 1, "First Title", "First Body"),
            Post(2, 2, "Second Title", "Second Body")
        )
    )


    override fun get(postId: Int): Observable<Post> =
        Observable.just(Post(1, 1, "Test", "Test"))

    override fun getPostComments(postId: Int): Observable<List<Comments>> =
        Observable.just(
            listOf(
                Comments(1, 1, "Ali Jaber", "ali@gmail.com", "Good"),
                Comments(2, 2, "Mohammad Ali", "mali@gmail.com", "Hello")
            )
        )

    override fun getUserById(userId: Int): Observable<User> = Observable.just(
        User(
            1,
            "Ali Jaber",
            "Aly",
            "ali@gmail.com",
            null,
            "+9627888888",
            "www.example.com",
            null
        )
    )

    override fun create(post: Post): Observable<Post> =
        Observable.just(Post(1, 1, "First Title", "First Body"))

    override fun deleteById(postId: Int): Observable<Post> =
        Observable.just(Post(1, 1, "First Title", "First Body"))
}