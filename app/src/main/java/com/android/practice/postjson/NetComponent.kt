package com.android.practice.postjson

import com.android.practice.postjson.di.NetModule
import dagger.Component


@Component(modules = [ApplicationModule::class, NetModule::class])
interface NetComponent {
    fun injectMainActivity(mainActivity: MainActivity?)
}