package elfak.mosis.health.services

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import elfak.mosis.capturetheflag.utils.helpers.sendNotification
import elfak.mosis.health.R
import elfak.mosis.health.ui.user.model.AuthState
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper._id
import org.json.JSONObject

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("ON_NEW_TOKEN", "Refreshed token: $token")

        //http
        val queue = Volley.newRequestQueue(this)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/PutFcm"
        val url2 = "http://localhost:5000/api/Gateway/PutFcm"

        val putData = JSONObject()
        putData.put("fcm", token)
        val prefs = SharedPreferencesHelper.customPreference(this, "Id")
        putData.put("_id", prefs._id)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url2, putData,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                AuthState.AuthError(error.message)
            }
        )

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: ${remoteMessage.from}")


        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Firebase", "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(this, it.body.toString(), R.drawable.heart)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }


}