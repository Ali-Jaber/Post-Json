package com.android.practice.postjson.di

import android.content.Context
import com.android.practice.postjson.*
import dagger.Component


@Component(modules = [ApplicationModule::class, NetModule::class])
interface NetComponent {

    companion object {

        @Volatile
        private var component: NetComponent? = null

        @Synchronized
        fun getComponent(context: Context): NetComponent =
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
}