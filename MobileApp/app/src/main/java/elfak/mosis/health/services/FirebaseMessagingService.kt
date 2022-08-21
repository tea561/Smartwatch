package elfak.mosis.health.services

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import elfak.mosis.capturetheflag.utils.helpers.sendNotification
import elfak.mosis.health.R

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("ON_NEW_TOKEN", "Refreshed token: $token")
        //TODO: sendRegistrationToServer
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