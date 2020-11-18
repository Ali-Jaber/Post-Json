package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.adapter.PostViewHolder
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject


open class MainActivity : BaseActivity() {

//    lateinit var appComponent: AppComponent
//        private set
//
//    companion object {
//        lateinit var instance: MainActivity
//            private set
//    }


//    @JvmField
//    @Inject
//    var retrofit: Retrofit?= null

//    @JvmField
//    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

    //    private val postApiService by lazy {
//        RetrofitModule.getApiClient().create(PostApiService::class.java)
//    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: GenericAdapter<Post, PostViewHolder>
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).netComponent!!.injectMainActivity(this)
        postApiService = retrofit.create(PostApiService::class.java)
//        postApiService = retrofit?.create(PostApiService::class.java)

//        initComponent()
        initToolbar()
        title = getString(R.string.posts)
        initList()
        loadData()

//        (application as CustomApplication).getAppComponent()?.inject(this@MainActivity)

//        x = retrofit.create(PostApiService::class.java)
//        appComponent.inject(instance)

        postSwipeRefresh.setOnRefreshListener {
            loadData()
            postSwipeRefresh.isRefreshing = false
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

//    private fun initComponent() {
//        appComponent = DaggerAppComponent.builder()
//            .build()
//        appComponent.inject(this)
//    }

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
//                        .apply {
//                            putExtra(POST_ID, item.id)
//                        }.also {
//                            startActivity(it)
//                        }
                    intent.putExtra(POST_ID, item.id)
                    startActivity(intent)
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