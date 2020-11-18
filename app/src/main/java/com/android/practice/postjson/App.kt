package com.android.practice.postjson

import android.app.Application
import com.android.practice.postjson.di.NetModule
import com.android.practice.postjson.util.BASE_URL


class App : Application() {
    var netComponent: NetComponent? = null

    override fun onCreate() {
        super.onCreate()
        netComponent = DaggerNetComponent.builder()
            .applicationModule(ApplicationModule(this))
            .netModule(NetModule)
            .build()
    }

}