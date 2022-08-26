package elfak.mosis.health.ui.friends.model

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
import com.google.gson.Gson
import com.google.gson.JsonElement
import elfak.mosis.health.ui.user.data.User
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper._id
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FriendsViewModel : ViewModel() {

    private val _sendRequestState by lazy { MutableLiveData<SendRequestState>(SendRequestState.Idle)}
    var sendRequestState: LiveData<SendRequestState> = _sendRequestState

    private var _friends = MutableLiveData<MutableList<User>>()
    var friends: LiveData<MutableList<User>> = _friends

    fun getFriends(currentUserUid: String, context:Context) {
        val prefs = SharedPreferencesHelper.customPreference(context, "First time")
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url = "http://localhost:5000/api/Gateway/GetFriends/${prefs._id}"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP",    "Response: %s".format(response.toString()))
                if(_friends.value == null)
                    _friends.value = mutableListOf()
                val list = _friends.value
                for(i in 0 until response.length()){
                    val obj = response.getJSONObject(i)
                    val element: JsonElement =
                        Gson().fromJson(obj.toString(), JsonElement::class.java)
                    Log.i("JSON", element.toString())
                    val friend = Gson().fromJson(element, User::class.java)

                    val m: Int? = list?.indexOfFirst { u -> u._id == friend?._id }
                    if(m == null || m == -1)
                        list?.add(friend)
                }
                _friends.value = list
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun addFriend(friendId: String, context: Context) {
        val prefs = SharedPreferencesHelper.customPreference(context, "First time")
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url2 = "http://localhost:5000/api/Gateway/AddFriend/${prefs._id}/${friendId}"
        Log.i("url", url2)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url2, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                _sendRequestState.value = SendRequestState.Success()
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _sendRequestState.value = SendRequestState.SendRequestError(error.message)
            }
        )
        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }
}

sealed class SendRequestState {
    object Idle : SendRequestState()
    class Success(val message: String = "Successful"): SendRequestState()
    class SendRequestError(val message: String? = null): SendRequestState()
}
