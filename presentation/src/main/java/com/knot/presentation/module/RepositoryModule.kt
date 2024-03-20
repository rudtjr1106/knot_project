package com.knot.presentation.module

import com.knot.data.repository.SignRepositoryImpl
import com.knot.domain.repository.SignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideSignRepository(signRepositoryImpl: SignRepositoryImpl): SignRepository
}