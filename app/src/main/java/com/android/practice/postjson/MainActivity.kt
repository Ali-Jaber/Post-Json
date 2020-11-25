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
import com.android.practice.postjson.util.ADD_POST
import com.android.practice.postjson.util.POST_DELETE
import com.android.practice.postjson.util.POST_DETAILS
import com.android.practice.postjson.util.POST_ID
import com.google.android.material.snackbar.Snackbar
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
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val post: Post = data?.extras?.getParcelable<Parcelable>("post") as Post
                postAdapter.addItem(post)
                showSnackbar("Post Added")
            } else if (requestCode == 2) {
                val post = data?.extras?.getParcelable<Parcelable>(POST_DELETE) as Post
                postAdapter.remove(post)
                showSnackbar("Post Deleted")
            }
        }
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
                    Intent(this@MainActivity, PostDetailsActivity::class.java)
                        .apply {
                            putExtra(POST_ID, item.id)
                        }.also {
                            startActivityForResult(it, POST_DETAILS)
                        }
                }
            }
        }
        recyclerView.adapter = postAdapter
    }

    private fun loadData() {
        postSwipeRefresh.isRefreshing = true
        disposable.add(
            postApiService.getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    setDataInRecyclerView(it);
                    postSwipeRefresh.isRefreshing = false
                }, {
                    Log.e("error", "errors", it)
                })
        )
    }

    private fun setDataInRecyclerView(it: List<Post>) {
        postAdapter.addAll(it)
    }

    private fun initView() {
        fb_addPost.setOnClickListener {
            Intent(this, AddPostActivity::class.java)
                .also {
                    startActivityForResult(it, ADD_POST)
                }
        }

        postSwipeRefresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            mainView, message,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
    }
}