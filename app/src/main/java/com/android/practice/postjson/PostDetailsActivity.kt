package com.android.practice.postjson

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.model.User
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_DELETE
import com.android.practice.postjson.util.POST_ID
import com.android.practice.postjson.util.USER_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_details.*
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject


class PostDetailsActivity : BaseActivity() {

    private val progressDialog by lazy { CustomProgressDialog() }

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

//    private val viewModel: PostDetailsViewModel by lazy {
//        ViewModelProviders.of(this).get(PostDetailsViewModel::class.java)
//    }


    private lateinit var loadingDialog: LoadingDialog
    private val disposable = CompositeDisposable()
    private var postId: Int? = 0
    private var userId: Int? = 0
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        NetComponent.getComponent(this).inject(this)
        initToolbar()
        loadingDialog = LoadingDialog(this@PostDetailsActivity)
        title = getString(R.string.post_details_title)

//        viewModel.getPostDetails().observe(this, Observer { t ->
//            setDataInRecyclerView(t.)
//        })

        postId = intent.extras?.getInt(POST_ID)
        userId = intent.extras?.getInt(USER_ID)
        loadData(postId)
        handelButton(postId)
        getUser(userId)

    }

//    private fun setDataInRecyclerView(it: Post) {
//        postDetails_title_textView.text = it.title
//        postDetails_body_textView.text = it.body
//        userId = it.userId
//        post = Post(it.userId, it.id, it.title, it.body)
//    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                showAlert()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_post)
            .setCancelable(true)
            .setMessage(R.string.delete_post_question)
            .setPositiveButton(R.string.yes) { _, _ ->
                deletePost()
            }
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .show()
    }

    private fun deletePost() {
        postApiService.deletePost(postId)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loadingDialog.startLoadingDialog() }
            .subscribe({ ps ->
                Intent().apply {
                    putExtra(POST_DELETE, post)
                }.also {
                    setResult(RESULT_OK, it)
                    loadingDialog.dismissDialog()
                    finish()
                }
            }, {
                Log.e("error", "errors", it)
            })

    }

    private fun loadData(postId: Int?) {
        disposable.add(
            postApiService.getPostDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    loadingDialog.startLoadingDialog()
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postDetails_title_textView.text = it.title
                    postDetails_body_textView.text = it.body
                    userId = it.userId
                    post = Post(it.userId, it.id, it.title, it.body)
//                    progressDialog.dialog.dismiss()
                    loadingDialog.dismissDialog()
                }, {
                    Log.e("error", "errors", it)
                    loadingDialog.dismissDialog()
                })
        )
    }

    private fun getUser(userId: Int?) {
        disposable.add(
            postApiService.getUser(userId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postDetails_name_textView.text = it.name
                }, {
                    Log.e("error", "errors", it)
                })
        )
    }

    private fun requestResults() {
//        Observable.zip(
//            postApiService.getPostDetails(1),
//            postApiService.getUser(1),
//            Function2<<Post>, <User>, Unit> {
//            response1, response2 ->
//            this.response1 = response1
//            this.response2 = response2
//            )
        val postDetails: Observable<Post> = postApiService.getPostDetails(1)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
        val user: Observable<User> = postApiService.getUser(1)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

//        val combined: Observable<PostAndUser> = Observable.zip(
//            postDetails,
//            user,
//            object : Func2<Post?, User?, PostAndUser?>() {
//                fun call(post: Post?, user: User?): PostAndUser? {
//                    return PostAndUser(post, user)
//                }
//            })

        Observable.merge(postDetails, user)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it as Post
                it.title
                it as User
                it.name
            },
                {
                    Log.e("error", "errors", it)
                }
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