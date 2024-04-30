package com.example.testandroid2

import android.app.Application
import com.example.testandroid2.di.repositoryModule
import com.example.testandroid2.di.roomModule
import com.example.testandroid2.di.vmModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(roomModule, repositoryModule, vmModule))
        }
    }
}
