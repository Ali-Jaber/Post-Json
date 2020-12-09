package com.android.practice.postjson.di

import com.android.practice.postjson.*
import com.android.practice.postjson.contract.PostPresenter
import com.android.practice.postjson.ui.PostDetailsViewModel
import dagger.Component


@Component(modules = [ApplicationModule::class, NetModule::class])
interface NetComponent {

    companion object {

        @Volatile
        private var component: NetComponent? = null

        @Synchronized
        fun getComponent(): NetComponent =
            component
                ?: DaggerNetComponent.builder()
                    .build()
                    .also {
                        component = it
                    }
    }

    fun inject(activity: MainActivity)
    fun inject(activity: CommentsActivity)
    fun inject(activity: PostDetailsActivity)
    fun inject(activity: AddPostActivity)
    fun inject(postDetailsViewModel: PostDetailsViewModel)
    fun inject(postPresenter: PostPresenter)
}