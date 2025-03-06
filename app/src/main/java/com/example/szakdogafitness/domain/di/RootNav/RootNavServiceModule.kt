package com.example.szakdogafitness.domain.di.RootNav

import com.example.szakdogafitness.data.root.IRootNavService
import com.example.szakdogafitness.data.root.RootNavService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RootNavServiceModule{


    @Binds
    @Singleton
    abstract fun bindRootNavService(
        rootNavService: RootNavService
    ) : IRootNavService


}