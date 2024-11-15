package com.padym.rusread

import android.app.Application
import com.padym.rusread.data.AppDatabase

lateinit var db: AppDatabase

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }
}