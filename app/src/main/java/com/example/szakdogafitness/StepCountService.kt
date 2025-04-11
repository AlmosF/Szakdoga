package com.example.szakdogafitness

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.content.ContextCompat.getSystemService
import com.example.szakdogafitness.data.stepcount.IStepCountRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepCountService : Service(), SensorEventListener {

    @Inject lateinit var repository: IStepCountRepository

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var initialSteps = -1

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        startForeground(1, createNotification())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            initialSteps = repository.getInitialSensorSteps()
            if (initialSteps == -1){
                val tempListener = object : SensorEventListener {
                    var initialEvent: SensorEvent? = null

                    override fun onSensorChanged(event: SensorEvent?) {
                        initialEvent = event
                        sensorManager.unregisterListener(this)
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                }

                stepSensor?.let {
                    sensorManager.registerListener(tempListener, it, SensorManager.SENSOR_DELAY_NORMAL)
                }


                delay(100)

                val totalSteps = tempListener.initialEvent?.values?.firstOrNull()?.toInt() ?: -1

                if (totalSteps != -1){
                    initialSteps = totalSteps
                    repository.saveInitialSensorSteps(initialSteps)
                }

                stepSensor?.let {
                    sensorManager.registerListener(this@StepCountService, it, SensorManager.SENSOR_DELAY_NORMAL)
                }
            } else {
                stepSensor?.let {
                    sensorManager.registerListener(this@StepCountService, it, SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        }

        return START_STICKY
    }
    /*override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        return START_STICKY
    }*/

    /*override fun onSensorChanged(event: SensorEvent?) {
        val totalSteps = event?.values?.firstOrNull()?.toInt() ?: return
        if (initialSteps == -1) initialSteps = totalSteps
        val delta = totalSteps - initialSteps
        Log.d("StepCountService", "Steps: $delta")
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveStepsLocally(delta)
        }
    }*/
    /*override fun onSensorChanged(event: SensorEvent?) {
        val totalSteps = event?.values?.firstOrNull()?.toInt() ?: return
        //if (initialSteps == -1) initialSteps = totalSteps
        val delta = totalSteps - initialSteps
        Log.d("StepCountService", "Total Steps: $totalSteps, Initial Steps: $initialSteps, Delta: $delta")

        // Debugging step to check
        Log.d("StepCountService", "Steps detected: $delta")

        if (delta > 0) {
            // Save the delta value to DataStore
            CoroutineScope(Dispatchers.IO).launch {
                repository.saveStepsLocally(delta)
            }
        }
    }*/
    override fun onSensorChanged(event: SensorEvent?) {
        val totalSteps = event?.values?.firstOrNull()?.toInt() ?: return
        if (initialSteps == -1) {
            CoroutineScope(Dispatchers.IO).launch {
                initialSteps = repository.getInitialSensorSteps()
            }
            return;
        }

        val delta = totalSteps - initialSteps
        Log.d("StepCountService", "Total Steps: $totalSteps, Initial Steps: $initialSteps, Delta: $delta")

        if (delta > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.saveStepsLocally(delta)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val channelId = "step_counter_channel"
        val channel = NotificationChannel(channelId, "Step Counter", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Step Counter Running")
            .setContentText("Counting your steps")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }
}