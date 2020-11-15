package com.android.practice.postjson

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.practice.postjson.model.Post
import com.android.practice.postjson.util.POST_DETAILS
import com.android.practice.postjson.util.POST_ID
import kotlinx.android.synthetic.main.activity_post_details.*

class PostDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        initToolbar()
        title = "Post Details"

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val data = intent.extras
        val post: Post = data?.getParcelable<Parcelable>(POST_DETAILS) as Post

        postDetails_title_textView.text = post.title
        postDetails_body_textView.text = post.body

        comments_btn.setOnClickListener {
            val postId = post.id
            val commentIntent = Intent(this@PostDetailsActivity, CommentsActivity::class.java)
                .also {
                    it.putExtra(POST_ID, postId)
                }
            startActivity(commentIntent)
        }

    }
}