package com.android.practice.postjson.contract

import android.util.Log
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.services.jsonplaceholder.PostService
import com.android.practice.postjson.services.jsonplaceholder.PostServiceRemoteImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PostPresenter(private val mView: PostContract.View) : PostContract.Presenter {

    @Inject
    lateinit var apiService: PostApiService
    private val disposable = CompositeDisposable()
    private var postService: PostService

    init {
        NetComponent.getComponent().inject(this)
        postService = PostServiceRemoteImpl(apiService)
    }

    override fun start() {
        mView.init()
    }

    override fun loadPosts() {
        disposable.add(
            postService.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mView.loadDataInList(it)
                }, {
                    Log.e("error", "error", it)
                })
        )
    }

    override fun onViewDestroyed() {
        disposable.dispose()
    }
}