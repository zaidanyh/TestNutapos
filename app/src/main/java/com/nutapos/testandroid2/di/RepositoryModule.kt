package com.nutapos.testandroid2.di

import com.nutapos.testandroid2.datasource.MainRepositoryImpl
import com.nutapos.testandroid2.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMainRepository(repositoryImpl: MainRepositoryImpl): MainRepository
}
