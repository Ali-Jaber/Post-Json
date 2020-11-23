package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.network.PostApiService
import kotlinx.android.synthetic.main.activity_add_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AddPostActivity : BaseActivity() {

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        NetComponent.getComponent(this).inject(this)
        initToolbar()
        title = getString(R.string.add_post)
        btnAddPost.setOnClickListener {
            val title = editTextTitle.text.toString()
            val body = editTextBody.text.toString()
            val post = Post(2, 2, title, body)
            val loadData = loadData(post)
           loadData.enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        Log.e("onResponse", "${response.body()}")
                        Toast.makeText(this@AddPostActivity, "Post Added", Toast.LENGTH_SHORT)
                            .show()
                        Intent(this@AddPostActivity, MainActivity::class.java).apply {
                            Log.e("this", "$post")
                            putExtra("post", post)
                        }.also {
                            setResult(1, it)
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Log.e("onFailure", "${t.message}")
                }

            })
        }
    }

    private fun loadData(post: Post): Call<Post> {
        return postApiService.createPost(post)
    }
}