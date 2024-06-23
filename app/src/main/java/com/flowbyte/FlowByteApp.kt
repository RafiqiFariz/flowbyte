package com.flowbyte

import android.app.Application
import com.flowbyte.core.AppPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlowByteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}