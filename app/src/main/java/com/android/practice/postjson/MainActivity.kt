package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.adapter.PostViewHolder
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: GenericAdapter<Post, PostViewHolder>
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetComponent.getComponent(this).inject(this)
        initToolbar()
        title = getString(R.string.posts)
        initList()
        loadData()
        fb_addPost.setOnClickListener {
            Intent(this, AddPostActivity::class.java)
                .also {
                    startActivityForResult(it,1)
                }
        }

        postSwipeRefresh.setOnRefreshListener {
            loadData()
            postSwipeRefresh.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val post: Post = data?.extras?.getParcelable<Parcelable>("post") as Post
        postAdapter.addItem(post)
    }
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun initList() {
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
                    val intent = Intent(this@MainActivity, PostDetailsActivity::class.java)
                        .apply {
                            putExtra(POST_ID, item.id)
                        }.also {
                            startActivity(it)
                        }
                }
            }
        }
        recyclerView.adapter = postAdapter
    }

    private fun loadData() {
        disposable.add(
            postApiService.getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    Log.d("size", it.size.toString())
                    setDataInRecyclerView(it);
                }, {
                    Log.d("error", "errors")
                })
        )
    }

    private fun setDataInRecyclerView(it: List<Post>) {
        postAdapter.addAll(it)
    }
}