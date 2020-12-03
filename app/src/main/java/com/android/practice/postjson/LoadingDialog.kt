package com.android.practice.postjson

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

class LoadingDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater

        builder.apply {
            setView(inflater.inflate(R.layout.custom_dialog, null))
            setCancelable(false)

            dialog = builder.create()
            dialog.show()

        }
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}