package com.android.practice.postjson.ui.post

import android.util.Log
import com.android.practice.postjson.base.BasePresenter
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.network.PostApiService
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class PostPresenter(postView: PostView) : BasePresenter<PostView>(postView) {

    @Inject
    lateinit var postApiService: PostApiService
    private var subscription: Disposable? = null
    override fun onViewCreated() {
//        NetComponent.getComponent(this).inject(this)
        loadPosts()
    }

    fun loadPosts() {
        postApiService.getPosts()
            .observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ postList ->
                view.updatePosts(postList)
            }, {
                Log.e("error", "errors", it)
            })


    }

//    fun loadPosts2() {
//        postApiService
//            .getPosts()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                { postList -> view.updatePosts(postList) })
//    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
    }
}