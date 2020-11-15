package com.android.practice.postjson

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.CommentsViewHolder
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.di.RetrofitModule
import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CommentsActivity : BaseActivity() {

    private val apiService by lazy {
        RetrofitModule.getApiClient().create(PostApiService::class.java)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: GenericAdapter<Comments, CommentsViewHolder>
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        initToolbar()
        title = "Comments"
        window.decorView.systemUiVisibility = View  .SYSTEM_UI_FLAG_FULLSCREEN

        val postId = intent.extras?.getInt(POST_ID)
        recyclerView = findViewById(R.id.comment_recyclerView)
        commentsAdapter = object : GenericAdapter<Comments, CommentsViewHolder>() {

            override fun createVm(parent: ViewGroup, viewType: Int): CommentsViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.comments_item, parent, false)
                return CommentsViewHolder(view)
            }

            override fun bindVm(holder: CommentsViewHolder, position: Int, item: Comments) {
                holder.name.text = item.name
                holder.body.text = item.body
                holder.email.text = item.email
            }
        }

        recyclerView.adapter = commentsAdapter

        disposable.add(
            apiService.getCommentsDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("size", it.size.toString())
                    setDataInRecyclerView(it);
                }, {
                    Log.d("error", "errors")
                })
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun setDataInRecyclerView(it: List<Comments>) {
        commentsAdapter.addAll(it)
    }
}