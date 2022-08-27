package elfak.mosis.health.ui.stepcounter

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import java.text.SimpleDateFormat

class StepCounterService : Service(), SensorEventListener {

    public val BROADCAST_ACTION: String = "broadcast_steps"
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null

    var currentStepsDetected: Int = 0

    var stepCount: Int = 0
    var newStepCount: Int = 0


    lateinit var intent: Intent


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            Log.i("STEP-COUNT", "Step Count Service is running in the background.")
        }.start()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCounterSensor?.let {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
        stepDetectorSensor?.let {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }



        return START_STICKY

    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Return the communication channel to the service.")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {

                val prefs = SharedPreferencesHelper.customPreference(this, "First time")


                event.values.firstOrNull()?.let {
                    Log.i("STEP-COUNTER", "Step count: $it ")
                    Log.i("STEP-COUNTER", "Shared pref: ${prefs.stepCount}")

                    val count = it.toInt()
                    if(stepCount == 0)
                        stepCount = count
                    newStepCount = count - stepCount
                    prefs.stepCount = newStepCount
                    // Data 2: The number of nanosecond passed since the time of last boot
                    val lastDeviceBootTimeInMillis =
                        System.currentTimeMillis() - SystemClock.elapsedRealtime()
                    val sensorEventTimeInNanos =
                        event.timestamp // The number of nanosecond passed since the time of last boot
                    val sensorEventTimeInMillis = sensorEventTimeInNanos / 1000_000

                    val actualSensorEventTimeInMillis =
                        lastDeviceBootTimeInMillis + sensorEventTimeInMillis
                    val displayDateStr =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualSensorEventTimeInMillis)
                    Log.i("STEP-COUNTER", "Sensor event is triggered at $displayDateStr")

                    val intent = Intent(BROADCAST_ACTION)
                    intent.putExtra("steps", prefs.stepCount)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                }
            }

            if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR)
            {
                event.values.firstOrNull()?.let {
                    Log.i("STEP-DETECTOR", "Step detected: $it")

                }
            }

        }


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }




    companion object {
        fun checkPermission(context: Context): Boolean {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED
            else
                true
        }
    }



}