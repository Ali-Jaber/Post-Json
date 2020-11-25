package com.android.practice.postjson

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.POST_DELETE
import com.android.practice.postjson.util.POST_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_details.*
import retrofit2.Retrofit
import javax.inject.Inject

class PostDetailsActivity : BaseActivity() {

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

    private val disposable = CompositeDisposable()
    private var postId: Int? = 0
    private var post: Post? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        NetComponent.getComponent(this).inject(this)
        initToolbar()
        title = getString(R.string.post_details_title)

        postId = intent.extras?.getInt(POST_ID)
        loadData(postId)
        handelButton(postId)
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
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
            .setTitle("Delete Post")
            .setMessage("Do you need delete this post?")
            .setPositiveButton("Delete") { _, _ ->
                postApiService.deletePost(postId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({ ps ->
                        Intent().apply {
                            Log.e("deletepost", "$ps")
                            Log.e("deletepost", "$postId")
                            putExtra(POST_DELETE, post)
                        }.also {
                            setResult(RESULT_OK, it)
                            finish()
                        }
                    }, {
                        Log.e("error", "errors", it)
                    })
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "Cancel is pressed", Toast.LENGTH_LONG).show()
            }
            .show()
    }
    private fun loadData(postId: Int?) {
        disposable.add(
            postApiService.getPostDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postDetails_title_textView.text = it.title
                    postDetails_body_textView.text = it.body
                    post = Post(it.userId,it.id,it.title,it.body)
                }, {
                    Log.e("error", "errors", it)
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