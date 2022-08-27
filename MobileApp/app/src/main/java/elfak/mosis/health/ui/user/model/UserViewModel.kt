package elfak.mosis.health.ui.user.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.*
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.user.data.User
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper._id
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.firstTime
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel : ViewModel() {
    var currentUser: User? = null

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    private val _fetchingUserState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle)}
    val fetchingUserState: LiveData<FetchingState> = _fetchingUserState

    fun getUser(context: Context, _id: Int)
    {
        _fetchingUserState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetUser/$_id"
        val url = "http://localhost:5000/api/Gateway/GetUser/$_id"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                val element: JsonElement =
                    Gson().fromJson(response.toString(), JsonElement::class.java)
                Log.i("JSON", element.toString())
                val user = Gson().fromJson(element, User::class.java)
                currentUser = user
                val prefs = SharedPreferencesHelper.customPreference(context, "First time")
                prefs._id = currentUser!!._id
                _fetchingUserState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingUserState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonObjectRequest)
    }

    fun createUser(context: Context, user: User){
        _authState.value = AuthState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/PostUser"
        val url2 = "http://localhost:5000/api/Gateway/PostUser"

        val jsonstr: String = Gson().toJson(user)
        Log.i("USER", jsonstr)
        val obj = JSONObject(jsonstr)
        Log.i("USER", obj.toString())


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url2, obj,
                Response.Listener { response ->
                    Log.i("HTTP", "Response: %s".format(response.toString()))

                    currentUser = user
                    currentUser!!._id = response["result"] as Int
                    val prefs = SharedPreferencesHelper.customPreference(context, "First time")
                    prefs.firstTime = false
                    prefs._id = currentUser!!._id
                    _authState.value = AuthState.Success
                },
                Response.ErrorListener { error ->
                    Log.i("HTTP", "Error: ${error.toString()}")
                    AuthState.AuthError(error.message)
                }
            )


        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }

    fun addFcm(context: Context, fcm: String){
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/PostFcm"
        val url2 = "http://localhost:5000/api/Gateway/PostFcm"

        val postData = JSONObject()
        postData.put("fcm", fcm)
        currentUser?.let { postData.put("_id", it._id) }
        Log.i("HTTP FCM", postData.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url2, postData,
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


}
sealed class AuthState {
    object Idle : AuthState()
    /*    object Loading : AuthState()*/
    object Success : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}
