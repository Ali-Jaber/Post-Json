package com.android.practice.postjson

import android.app.ProgressDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.postjson.adapter.CommentsViewHolder
import com.android.practice.postjson.adapter.GenericAdapter
import com.android.practice.postjson.di.NetComponent
import com.android.practice.postjson.databinding.ActivityCommentsBinding
import com.android.practice.postjson.model.Comments
import com.android.practice.postjson.network.PostApiService
import com.android.practice.postjson.util.PLEASE_WAIT
import com.android.practice.postjson.util.POST_ID
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyFooterItem
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comments.*
import retrofit2.Retrofit
import javax.inject.Inject

class CommentsActivity : AppCompatActivity() {

//    private val progressDialog by lazy { CustomProgressDialog() }

    private lateinit var binding: ActivityCommentsBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var headerView: AccountHeaderView
    private lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var postApiService: PostApiService

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: GenericAdapter<Comments, CommentsViewHolder>
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_comments)
        binding = ActivityCommentsBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        NetComponent.getComponent(this).inject(this)
//        initToolbar()
        title = getString(R.string.comments_title)
        loadingDialog = LoadingDialog(this@CommentsActivity)

        val postId = intent.extras?.getInt(POST_ID)

        initDrawer(savedInstanceState)

        initList()
        loadData(postId)

        commentSwipeRefresh.setOnRefreshListener {
            loadData(postId)
        }
    }

    private fun initDrawer(savedInstanceState: Bundle?) {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.root,
            binding.toolbar,
            R.string.material_drawer_open,
            R.string.material_drawer_close
        )

        val item1 = PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home)
        val item2 = SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings)

        binding.slider.apply {
            itemAdapter.add(
                item1,
                DividerDrawerItem(),
                item2,
                SecondaryDrawerItem().withName(R.string.drawer_item_settings)
            )
        }

        binding.slider.apply {
            setSelection(1)
            setSelection(2)
            addItems(DividerDrawerItem())
            addStickyFooterItem(PrimaryDrawerItem().withName("StickyFooterItem"))
            drawerLayout?.openDrawer(slider)
            drawerLayout?.closeDrawer(slider)
        }

        val profile = ProfileDrawerItem().apply {
            nameText = "Ali Jaber"; descriptionText = "alijaber@gmail.com"; iconRes =
            R.drawable.profile; badgeText = "123"
            badgeStyle = BadgeStyle().apply {
                textColor = ColorHolder.fromColor(Color.WHITE)
                color = ColorHolder.fromColorRes(R.color.md_red_700)
            }
        }

        headerView = AccountHeaderView(this).apply {
            attachToSliderView(slider)
            addProfiles(
                profile
            )
            withSavedInstance(savedInstanceState)
        }
        binding.slider.drawerLayout
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        outState = binding.slider.saveInstanceState(outState)
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(binding.slider)) {
            binding.root.closeDrawer(binding.slider)
        } else {
            super.onBackPressed()
        }
    }

    private fun initList() {
        recyclerView = findViewById(R.id.comment_recyclerView)
        commentsAdapter = object : GenericAdapter<Comments, CommentsViewHolder>() {

            override fun createVm(parent: ViewGroup, viewType: Int): CommentsViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.comments_item, parent, false)
                return CommentsViewHolder(view)
            }

            override fun bindVm(holder: CommentsViewHolder, position: Int, item: Comments) {
                holder.name.text = item.name
                holder.body.text = item.body
                holder.email.text = item.email
            }
        }
        recyclerView.adapter = commentsAdapter
    }

    private fun loadData(postId: Int?) {
        commentSwipeRefresh.isRefreshing = true
        disposable.add(
            postApiService.getCommentsDetails(postId!!)
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    loadingDialog.startLoadingDialog()
//                }
                .subscribeOn(Schedulers.io())
//                .doOnTerminate {
//                }
                .subscribe({
                    setDataInRecyclerView(it)
//                    progressDialog.dialog.dismiss()
                    commentSwipeRefresh.isRefreshing = false
                    loadingDialog.dismissDialog()
                }, {
                    commentSwipeRefresh.isRefreshing = false
                    Log.d("error", "errors", it)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun setDataInRecyclerView(it: List<Comments>) {
        commentsAdapter.addAll(it)
    }
}