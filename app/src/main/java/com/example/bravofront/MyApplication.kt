package com.example.bravofront

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.example.bravofront.api.ARQUIVO_LOGIN

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    private var activeActivities = 0

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        activeActivities++
    }

    override fun onActivityStopped(activity: Activity) {
        activeActivities--
        if (activeActivities == 0) {
            onAppClosed()
        }
    }

    private fun onAppClosed() {
        val sp = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        val edit = sp.edit()

        if(!sp.getBoolean("keepLogin", false)) {
            edit.remove("user")
            edit.remove("email")
            edit.remove("password")
        }

        edit.apply()
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
