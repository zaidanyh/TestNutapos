package com.example.testandroid2.di

import com.example.testandroid2.datasource.MainRepository
import com.example.testandroid2.datasource.local.room.TestDatabase
import com.example.testandroid2.datasource.local.room.MutationDao
import com.example.testandroid2.presentation.MainViewModel
import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomModule = module {
    single { provideDatabase(androidContext()) }
    single { provideMutationDao(get()) }
}

val repositoryModule = module {
    single { MainRepository(get()) }
}

val vmModule = module {
    viewModel { MainViewModel(get()) }
}

fun provideDatabase(context: Context): TestDatabase =
    Room.databaseBuilder(context, TestDatabase::class.java, "test_database").build()

fun provideMutationDao(db: TestDatabase): MutationDao = db.mutationDao()
