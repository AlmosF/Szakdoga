package com.example.szakdogafitness.data.location

import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng
import kotlinx.coroutines.flow.Flow

interface ILocationService {


    fun requestLocationUpdates(): Flow<GeoPoint?>

    fun requestCurrentLocation(): Flow<GeoPoint?>

    //fun requestLocationUpdates(): Flow<LatLng?>
    //fun requestCurrentLocation(): Flow<LatLng?>

}