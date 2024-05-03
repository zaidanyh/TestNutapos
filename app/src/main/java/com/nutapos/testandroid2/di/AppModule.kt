package com.nutapos.testandroid2.di

import android.content.Context
import androidx.room.Room
import com.nutapos.testandroid2.datasource.local.room.MutationDao
import com.nutapos.testandroid2.datasource.local.room.TestDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TestDatabase =
        Room.databaseBuilder(context, TestDatabase::class.java, "test_database").build()

    @Provides
    @Singleton
    fun provideMutationDao(db: TestDatabase): MutationDao = db.mutationDao()
}
