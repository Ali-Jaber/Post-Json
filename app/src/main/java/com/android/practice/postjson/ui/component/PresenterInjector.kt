package com.android.practice.postjson.ui.component

import com.android.practice.postjson.base.BaseView
import com.android.practice.postjson.di.NetModule
import com.android.practice.postjson.di.module.ContextModule
import com.android.practice.postjson.ui.post.PostPresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetModule::class])
interface PresenterInjector {

    fun inject(postPresenter: PostPresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}