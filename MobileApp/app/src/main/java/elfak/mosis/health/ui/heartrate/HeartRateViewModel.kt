package elfak.mosis.health.ui.heartrate

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.data.Entry
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HeartRateViewModel : ViewModel() {
    var weeklyEntries = ArrayList<Entry>()
    var dailyEntries = ArrayList<Entry>()

    private val _lastValue by lazy { MutableLiveData(0)}
    var lastValue: LiveData<Int> = _lastValue

    private val _lastTime by lazy { MutableLiveData(LocalDateTime.now())}
    var lastTime: LiveData<LocalDateTime> = _lastTime

    private val _fetchingWeeklyDataState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingWeeklyDataState: LiveData<FetchingState> = _fetchingWeeklyDataState

    private val _fetchingDailyDataState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingDailyDataState: LiveData<FetchingState> = _fetchingDailyDataState

    fun getWeeklyData(context: Context) {
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url = "http://localhost:5000/api/Gateway/GetPulseForWeek/9"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("value")
                    if(value.toString() == "null"){
                        weeklyEntries.add(Entry(i.toFloat(), 0f))
                    }
                    else {
                        weeklyEntries.add(Entry(i.toFloat(), (value as Double).toFloat()))
                    }
                }
                _fetchingWeeklyDataState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingWeeklyDataState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun getDailyData(context: Context) {
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url = "http://localhost:5000/api/Gateway/GetPulseForDay/9"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    val value = element.get("value")
                    val time = element.get("time")
                    val date = LocalDateTime.parse(time.toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    val hours = date.hour
                    Log.i("JSON", time.toString())
                    if(value.toString() == "null"){
                        dailyEntries.add(Entry(hours.toFloat(), 0f))
                    }
                    else {
                        dailyEntries.add(Entry(hours.toFloat(), (value as Int).toFloat()))
                    }
                }
                _fetchingDailyDataState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingDailyDataState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun updateLastValues(pulse: Int, time: Long){
        _lastValue.value = pulse
        _lastTime.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId())
    }
}

sealed class FetchingState {
    object Idle: FetchingState()
    object Success: FetchingState()
    class FetchingError(val message: String? = null) : FetchingState()
}