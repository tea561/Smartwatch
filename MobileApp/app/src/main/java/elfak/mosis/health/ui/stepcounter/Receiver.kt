package elfak.mosis.health.ui.stepcounter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import org.json.JSONObject
import java.io.Console
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class Receiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("RECEIVER", "HELLO")
        val prefs = p0?.let { SharedPreferencesHelper.customPreference(it, "Step_data") }
        if (prefs != null) {
            Log.i("RECEIVER", prefs.stepCount.toString())
//http
            val queue = Volley.newRequestQueue(p0)
            //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
            val url2 = "http://localhost:5000/api/Gateway/PostSteps"

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm ")
            val formatted = current.format(formatter)
            Log.i("FORMAT", formatted)


            println("Current Date and Time is: $formatted")
            val postData = JSONObject()
            postData.put("value", prefs.stepCount)
            postData.put("time", formatted)
            postData.put("userID", 51)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url2, postData,
                Response.Listener { response ->
                    Log.i("HTTP", "Response: %s".format(response.toString()))

                },
                Response.ErrorListener { error ->
                    Log.i("HTTP", "Error: ${error.toString()}")
                }
            )

            // Access the RequestQueue through your singleton class.
            queue.add(jsonObjectRequest)
            prefs.stepCount = 0
        }


    }
}