package com.nooblabs.conduit

import android.app.Application
import com.nooblabs.conduit.service.PersistenceRepository
import com.nooblabs.conduit.service.Service
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Service.init(applicationContext)
    }

}