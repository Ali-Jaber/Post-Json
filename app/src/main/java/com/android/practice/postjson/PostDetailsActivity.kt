package com.android.practice.postjson

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.ui.PostDetailsViewModel
import com.android.practice.postjson.util.POST_DELETE
import com.android.practice.postjson.util.POST_ID
import com.android.practice.postjson.util.USER_ID
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


    private lateinit var loadingDialog: LoadingDialog
    private var postId: Int? = 0
    private var userId: Int? = 0
    private var post: Post? = null
    private lateinit var viewModel: PostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        initToolbar()
        loadingDialog = LoadingDialog(this@PostDetailsActivity)
        title = getString(R.string.post_details_title)

        postId = intent.extras?.getInt(POST_ID)
        userId = intent.extras?.getInt(USER_ID)

        initViewModel()
        handelButton(postId)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PostDetailsViewModel::class.java)
        viewModel.apply {
            getPostDetails(postId)
            getUser(userId)
            postDetailsObserve()
            userObserve()
        }
    }

    private fun PostDetailsViewModel.postDetailsObserve() {
        postDetailsList.observe(this@PostDetailsActivity, {
            postDetails_title_textView.text = it.title
            postDetails_body_textView.text = it.body
            userId = it.userId
            post = Post(it.userId, it.id, it.title, it.body)
        })
    }

    private fun PostDetailsViewModel.userObserve() {
        user.observe(this@PostDetailsActivity, {
            postDetails_name_textView.text = it.name
        })
    }

    override fun onStop() {
        super.onStop()
        viewModel.onViewDestroy()
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