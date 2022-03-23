package com.albatros.kquiz.model.repo

import android.app.Application
import com.albatros.kquiz.model.module.appModule
import com.albatros.kquiz.model.module.repoModule
import com.albatros.kquiz.model.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class QuizApplication : Application() {

    private val modules = listOf(appModule, repoModule, viewModelModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuizApplication)
            modules(modules)
        }
    }
}