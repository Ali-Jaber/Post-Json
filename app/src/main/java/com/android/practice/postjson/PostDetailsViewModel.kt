package com.android.practice.postjson

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.practice.postjson.di.RetrofitModule
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PostDetailsViewModel : ViewModel() {

    private val postApiService by lazy {
        RetrofitModule.getApiClient().create(PostApiService::class.java)
    }

    var postDetailsList = MutableLiveData<Post>()

    fun getPostDetails(): MutableLiveData<Post> {
        getDetails()
        return postDetailsList
    }

    private fun getDetails() {
        postApiService.getPostDetails(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                postDetailsList.postValue(it)
            }, {
                Log.d("error", "errors", it)
            })
    }
}