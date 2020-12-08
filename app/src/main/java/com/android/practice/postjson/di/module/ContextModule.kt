package com.android.practice.postjson.di.module

import android.app.Application
import android.content.Context
import com.android.practice.postjson.base.BaseView
import dagger.Module
import dagger.Provides

@Module
object ContextModule {

    @Provides
    @JvmStatic
    internal fun provideContext(baseView: BaseView): Context = baseView.getContext()

    @Provides
    @JvmStatic
    internal fun provideApplication(context: Context): Application =
        context.applicationContext as Application

}