package com.developer_harshpatel.demo.ui.base

import android.app.Application
import android.util.Log
import android.widget.Toast

class App : Application() {

    // kotlin use companion object instead of static keyword
    companion object {
        lateinit var instance: App
            private set

        fun showToast(message: String) {
            Toast.makeText(instance, message, Toast.LENGTH_SHORT).show()
        }

        fun log(message: String) {
            Log.d("HHH", "" + message)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}