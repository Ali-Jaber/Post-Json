package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.PLEASE_WAIT
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_post.*
import retrofit2.Retrofit
import javax.inject.Inject

class AddPostActivity : BaseActivity() {

    private val progressDialog by lazy { CustomProgressDialog() }

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

    private var id = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        NetComponent.getComponent().inject(this)
        initToolbar()
        title = getString(R.string.add_post)
        initView()
    }

    private fun initView() {
        btnAddPost.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val post = Post(2, 2, textField.editText?.text.toString(), out.editText?.text.toString())
        postApiService.createPost(post)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                progressDialog.show(this, PLEASE_WAIT)
            }
            .subscribeOn(Schedulers.io())
            .subscribe({ resp ->
                Intent().apply {
                    Log.e("this", "$post")
                    putExtra("post", post)
                }.also {
                    setResult(RESULT_OK, it)
                    progressDialog.dialog.dismiss()
                    finish()
                }
            }, {
                Log.e("error", "errors", it)
            })
    }
}