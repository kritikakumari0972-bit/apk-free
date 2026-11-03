package com.example

import android.app.Application

class NovaBoostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NovaBoostApplication
            private set
    }
}
