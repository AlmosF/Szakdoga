package com.example.szakdogafitness.domain.di.RootNav

import com.example.szakdogafitness.data.root.IRootNavService
import com.example.szakdogafitness.domain.usecases.root.AllRootNavUseCases
import com.example.szakdogafitness.domain.usecases.root.RootNavigateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllRootNavUseCasesModule{


    @Provides
    @Singleton
    fun provideRootNavigateUseCase(repository: IRootNavService): RootNavigateUseCase {
        return RootNavigateUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideRootNavigationUseCases(
        repository: IRootNavService,
        rootNavigateUseCase: RootNavigateUseCase
    ): AllRootNavUseCases {
        return AllRootNavUseCases(
            repository,
            rootNavigateUseCase
        )
    }

}