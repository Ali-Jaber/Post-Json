package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.adapter.PostViewHolder
import com.android.practice.postjson.contract.PostContract
import com.android.practice.postjson.contract.PostContract.Presenter
import com.android.practice.postjson.contract.PostPresenter
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.services.jsonplaceholder.PostService
import com.android.practice.postjson.services.jsonplaceholder.PostServiceRemoteImpl
import com.android.practice.postjson.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject


class MainActivity : BaseActivity(), PostContract.View {

    private val progressDialog by lazy { CustomProgressDialog() }

//    @Inject
//    lateinit var postApiService: PostApiService
//    private val postService: PostService = PostServiceRemoteImpl(postApiService)

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var mPresenter: Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: GenericAdapter<Post, PostViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = PostPresenter(this)
        mPresenter.start()
        initToolbar()
        title = getString(R.string.posts)
    }

    override fun init() {
        recyclerView = findViewById(R.id.post_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        initAdapter()
        initView()
        mPresenter.loadPosts()
    }

    override fun showError(message: String) {
        Log.e("error", message)
    }

    override fun loadDataInList(posts: List<Post>) {
        postAdapter.addAll(posts)
    }

    private fun initAdapter() {
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
        recyclerView.adapter = postAdapter
        recyclerView.addItemDecoration(itemDecoration)
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
        mPresenter.onViewDestroyed()
    }

    private fun initView() {
        fb_addPost.setOnClickListener {
            Intent(this, AddPostActivity::class.java)
                .also {
                    startActivityForResult(it, ADD_POST)
                }
        }

        postSwipeRefresh.setOnRefreshListener {
            mPresenter.loadPosts()
        }
    }

    private fun setDataInRecyclerView(posts: List<Post>) {
        postAdapter.addAll(posts)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            mainView, message,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
    }
}