package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.adapter.PostViewHolder
import com.android.practice.postjson.adapter.SimpleItem
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.*
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject


class MainActivity : BaseActivity() {

    private val progressDialog by lazy { CustomProgressDialog() }

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
            when (requestCode) {
                ADD_POST -> {
                    data?.extras?.getParcelable<Parcelable>("post").also {
                        it as Post
                        postAdapter.addItem(it)
                        showSnackbar("Post Added")
                    }
                }
                POST_DETAILS -> {
                    data?.extras?.getParcelable<Parcelable>(POST_DELETE).also {
                        it as Post
                        postAdapter.remove(it)
                        showSnackbar("Post Deleted")
                    }

                }
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
                holder.body.text = item.body
                holder.itemView.setOnClickListener {
                    Intent(this@MainActivity, PostDetailsActivity::class.java)
                        .apply {
                            putExtra(POST_ID, item.id)
                            putExtra(USER_ID, item.userId)
                        }.also {
                            startActivityForResult(it, POST_DETAILS)
                        }
                }
            }
        }
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            .apply {
            setDrawable(getDrawable(R.drawable.divider)!!)
        }
//        itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
        recyclerView.adapter = postAdapter
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun loadData() {
        postSwipeRefresh.isRefreshing = true
        disposable.add(
            postApiService.getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    progressDialog.show(this, PLEASE_WAIT)
                }
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    setDataInRecyclerView(it);
                    postSwipeRefresh.isRefreshing = false
                    progressDialog.dialog.dismiss()
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