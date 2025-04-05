package com.example.szakdogafitness.domain.di.Location

import android.content.Context
import com.example.szakdogafitness.data.location.ILocationService
import com.example.szakdogafitness.data.location.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule{

    @Singleton
    @Provides
    fun provideLocationService(
        @ApplicationContext context: Context,
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ) : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

}