//package com.android.practice.postjson
//
//import android.app.Application
//import com.android.practice.postjson.di.RetrofitModule
//import dagger.BindsInstance
//import dagger.Component
//import dagger.android.AndroidInjector
//import javax.inject.Singleton
//
//@Component(modules = [RetrofitModule::class])
//@Singleton
//interface AppComponent: AndroidInjector<CustomApplication> {
//    fun inject(app: Application)
//
//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun application(application: Application): Builder
//
//        fun build(): AppComponent
//    }
//}