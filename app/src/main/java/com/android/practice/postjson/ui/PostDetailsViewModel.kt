package com.android.practice.postjson.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.model.User
import com.android.practice.postjson.network.PostApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PostDetailsViewModel : ViewModel() {

    @Inject
    lateinit var postApiService: PostApiService
    private val disposable = CompositeDisposable()
    var postDetailsList = MutableLiveData<Post>()
    var user = MutableLiveData<User>()

    init {
        NetComponent.getComponent().inject(this)
    }

    fun getPostDetails(postId: Int?): MutableLiveData<Post> {
        loadData(postId)
        return postDetailsList
    }

    private fun loadData(postId: Int?) {
        disposable.add(
            postApiService.getPostDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postDetailsList.value = it
                }, {
                    Log.e("error", "errors", it)
                })
        )
    }

    fun getUser(userId: Int?) {
        disposable.add(
            postApiService.getUser(userId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    user.value = it
                }, {
                    Log.e("error", "errors", it)
                })
        )
    }

    fun onViewDestroy() {
    disposable.dispose()
    }
}