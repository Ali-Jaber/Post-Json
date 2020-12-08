package com.android.practice.postjson.base

import com.android.practice.postjson.di.NetModule
import com.android.practice.postjson.di.module.ContextModule
import com.android.practice.postjson.ui.component.DaggerPresenterInjector
import com.android.practice.postjson.ui.component.PresenterInjector

abstract class BasePresenter<out V : BaseView>(protected val view: V) {

    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .networkModule(NetModule)
        .build()

    init {
        inject()
    }

    open fun onViewCreated() {}

    open fun onViewDestroyed() {}

    private fun inject() {

    }
}