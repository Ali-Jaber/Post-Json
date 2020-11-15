package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.adapter.PostViewHolder
import com.android.practice.postjson.di.RetrofitModule
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_DETAILS
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : BaseActivity() {

    private val postApiService by lazy {
        RetrofitModule.getApiClient().create(PostApiService::class.java)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: GenericAdapter<Post, PostViewHolder>
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        title = "Posts"
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        recyclerView = findViewById(R.id.post_recyclerView)
        postAdapter = object : GenericAdapter<Post, PostViewHolder>() {
            override fun createVm(parent: ViewGroup, viewType: Int): PostViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
                return PostViewHolder(view)
            }

            override fun bindVm(holder: PostViewHolder, position: Int, item: Post) {
                holder.title.text = item.title
                holder.itemView.setOnClickListener {
                    val postIntent = Intent(this@MainActivity, PostDetailsActivity::class.java)
                        .also {
                            it.putExtra(POST_DETAILS, item)
                        }
                    startActivity(postIntent)
                }
            }
        }
        recyclerView.adapter = postAdapter

        disposable.add(
            postApiService.getPosts()
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

    private fun setDataInRecyclerView(it: List<Post>) {
        postAdapter.addAll(it)
    }
}