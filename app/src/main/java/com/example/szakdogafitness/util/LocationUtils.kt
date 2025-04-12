package com.example.szakdogafitness.util

import android.util.Log
import com.google.firebase.firestore.GeoPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

fun GeoPoint.calculateDistance(other: GeoPoint?): Pair<Int?, Int?> {
    if (other == null) {
        return Pair(null, null)
    }
    if (this == other){
        return Pair(0,0)
    }
    Log.d("LocationUtils", "Calculating distance between $this and $other")
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(other.latitude - this.latitude)
    val dLon = Math.toRadians(other.longitude - this.longitude)
    Log.d("LocationUtils", "dLat: $dLat, dLon: $dLon")

    val lat1 = Math.toRadians(this.latitude)
    val lat2 = Math.toRadians(other.latitude)

    val a = sin(dLat / 2).pow(2) +
            sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val distanceKm = earthRadiusKm * c
    val distanceMiles = distanceKm * 0.621371

    Log.d("LocationUtils", "Distance: $distanceKm km, $distanceMiles miles")
    return Pair(distanceKm.roundToInt(), distanceMiles.roundToInt())
}