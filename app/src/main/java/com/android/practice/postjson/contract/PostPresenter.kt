package com.android.practice.postjson.contract

import android.util.Log
import com.android.practice.postjson.di.RetrofitModule
import com.android.practice.postjson.network.PostApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PostPresenter(private val mView: PostContract.View) : PostContract.Presenter {

    private val disposable = CompositeDisposable()

    private val apiService by lazy {
        RetrofitModule.getApiClient().create(PostApiService::class.java)
    }

    override fun start() {
        mView.init()
    }

    override fun loadPosts() {
        disposable.add(
            apiService.getPosts()
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