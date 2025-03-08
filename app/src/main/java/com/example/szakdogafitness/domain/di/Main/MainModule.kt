package com.example.szakdogafitness.domain.di.Main

import com.example.szakdogafitness.data.main.IMain
import com.example.szakdogafitness.data.main.Main
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule{


    @Binds
    @Singleton
    abstract fun bindMain(
        main: Main
    ): IMain

}