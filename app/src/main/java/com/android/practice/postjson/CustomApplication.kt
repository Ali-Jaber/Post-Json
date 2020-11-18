//package com.android.practice.postjson
//
//import android.app.Application
//import dagger.android.AndroidInjector
//import dagger.android.DispatchingAndroidInjector
//import dagger.android.HasAndroidInjector
//import javax.inject.Inject
//
//class CustomApplication : Application() {
//
////    @Inject
////    lateinit var androidInjector: DispatchingAndroidInjector<Any>
//    private var appComponent: AppComponent? = null
////    override fun onCreate() {
////        super.onCreate()
////        appComponent = DaggerAppComponent.builder()
////            .retrofitModule(RetrofitModule)
////            .build()
////    }
//
//    override fun onCreate() {
//        super.onCreate()
//        //DaggerComponent
//        //Build if you not see this
//        //  DaggerAppComponent.create().inject(this)
//
//        DaggerAppComponent.builder().application(this)!!.build().inject(this)
//    }
//
//    fun getAppComponent(): AppComponent? {
//        return appComponent
//    }
//
////    override fun androidInjector(): AndroidInjector<Any> = androidInjector
//
//
//}