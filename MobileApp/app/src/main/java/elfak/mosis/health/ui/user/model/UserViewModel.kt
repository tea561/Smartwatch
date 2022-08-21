package elfak.mosis.health.ui.user.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import elfak.mosis.health.ui.user.data.User
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.firstTime
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel : ViewModel() {
    var currentUser: User? = null

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    fun createUser(context: Context, user: User){
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url2 = "http://localhost:5000/api/Gateway/PostUser"

        val postData = JSONObject()
        val jsonstr: String = Gson().toJson(user)
        Log.i("USER", jsonstr)
        val obj = JSONObject(jsonstr)
        Log.i("USER", obj.toString())


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url2, obj,
                Response.Listener { response ->
                    Log.i("HTTP", "Response: %s".format(response.toString()))
                    _authState.value = AuthState.Success
                    currentUser = user
                    val prefs = SharedPreferencesHelper.customPreference(context, "First time")
                    prefs.firstTime = false
                },
                Response.ErrorListener { error ->
                    Log.i("HTTP", "Error: ${error.toString()}")
                    AuthState.AuthError(error.message)
                }
            )


        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }
}
sealed class AuthState {
    object Idle : AuthState()
    /*    object Loading : AuthState()*/
    object Success : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}
