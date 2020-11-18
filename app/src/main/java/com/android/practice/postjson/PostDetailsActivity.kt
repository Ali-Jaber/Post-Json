package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.android.practice.postjson.di.RetrofitModule
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_details.*

class PostDetailsActivity : BaseActivity() {

    private val apiService by lazy {
        RetrofitModule.getApiClient().create(PostApiService::class.java)
    }
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        initToolbar()
        title = getString(R.string.post_details_title)

        val postId = intent.extras?.getInt(POST_ID)

        loadData(postId)
        handelButton(postId)
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun loadData(postId: Int?) {
        disposable.add(
            apiService.getPostDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postDetails_title_textView.text = it.title
                    postDetails_body_textView.text = it.body
                }, {
                    Log.d("error", "errors")
                })
        )
    }

    private fun handelButton(postId: Int?) {
        comments_btn.setOnClickListener {
            Intent(this@PostDetailsActivity, CommentsActivity::class.java)
                .apply {
                    putExtra(POST_ID, postId)
                }.also {
                    startActivity(it)
                }
        }
    }

}