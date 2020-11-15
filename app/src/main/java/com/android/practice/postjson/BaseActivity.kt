package com.android.practice.postjson

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}