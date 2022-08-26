package elfak.mosis.health.ui.bloodpressure

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
import elfak.mosis.health.ui.heartrate.FetchingState
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class BloodPressureViewModel : ViewModel() {
    var weeklySysEntries = ArrayList<Entry>()
    var weeklyDiasEntries = ArrayList<Entry>()
    var dailySysEntries = ArrayList<Entry>()
    var dailyDiasEntries = ArrayList<Entry>()

    private val _lastSysValue by lazy { MutableLiveData(0)}
    var lastSysValue: LiveData<Int> = _lastSysValue

    private val _lastDiasValue by lazy { MutableLiveData(0)}
    var lastDiasValue: LiveData<Int> = _lastDiasValue

    private val _lastTime by lazy { MutableLiveData(LocalDateTime.now())}
    var lastTime: LiveData<LocalDateTime> = _lastTime



    private val _fetchingWeeklySysState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingWeeklySysState: LiveData<FetchingState> = _fetchingWeeklySysState

    private val _fetchingDailySysState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingDailySysState: LiveData<FetchingState> = _fetchingDailySysState

    private val _fetchingWeeklyDiasState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingWeeklyDiasState: LiveData<FetchingState> = _fetchingWeeklyDiasState

    private val _fetchingDailyDiasState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingDailyDiasState: LiveData<FetchingState> = _fetchingDailyDiasState


    fun getWeeklySysData(context: Context, _id: Int) {
        _fetchingWeeklySysState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameterForWeek/sys-pressure/$_id"
        val url = "http://localhost:5000/api/Gateway/GetParameterForWeek/sys-pressure/$_id"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("value")
                    if(value.toString() == "null"){
                        weeklySysEntries.add(Entry(i.toFloat(), 0f))
                    }
                    else {
                        weeklySysEntries.add(Entry(i.toFloat(), (value as Double).toFloat()))
                    }
                }
                _fetchingWeeklySysState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingWeeklySysState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun getDailySysData(context: Context, _id: Int) {
        _fetchingDailySysState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameterForDay/sys-pressure/$_id"
        val url = "http://localhost:5000/api/Gateway/GetParameterForDay/sys-pressure/$_id"

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
                        dailySysEntries.add(Entry(hours.toFloat(), 0f))
                    }
                    else {
                        dailySysEntries.add(Entry(hours.toFloat(), (value as Int).toFloat()))
                    }
                }
                _fetchingDailySysState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingDailySysState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun getWeeklyDiasData(context: Context, _id: Int) {
        _fetchingWeeklyDiasState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameterForWeek/dias-pressure/$_id"
        val url = "http://localhost:5000/api/Gateway/GetParameterForWeek/dias-pressure/$_id"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("value")
                    if(value.toString() == "null"){
                        weeklyDiasEntries.add(Entry(i.toFloat(), 0f))
                    }
                    else {
                        if(value is Double)
                            weeklyDiasEntries.add(Entry(i.toFloat(), (value as Double).toFloat()))
                        else if (value is Int)
                            weeklyDiasEntries.add(Entry(i.toFloat(), (value as Double).toFloat()))

                    }
                }
                _fetchingWeeklyDiasState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingWeeklyDiasState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun getDailyDiasData(context: Context, _id: Int) {
        _fetchingDailyDiasState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameterForWeek/dias-pressure/$_id"
        val url = "http://localhost:5000/api/Gateway/GetParameterForDay/dias-pressure/$_id"

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
                        dailyDiasEntries.add(Entry(hours.toFloat(), 0f))
                    }
                    else {
                        dailyDiasEntries.add(Entry(hours.toFloat(), (value as Int).toFloat()))
                    }
                }
                _fetchingDailyDiasState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingDailyDiasState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)
    }

    fun updateLastValues(sys: Int, dias: Int, timeString: Long){
        _lastSysValue.value = sys
        _lastDiasValue.value = dias
        _lastTime.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeString), TimeZone.getDefault().toZoneId())
    }

}