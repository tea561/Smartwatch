package elfak.mosis.health.ui.sleep

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
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import elfak.mosis.health.ui.heartrate.FetchingState
import java.util.concurrent.TimeUnit

class SleepViewModel: ViewModel() {
    var weeklySleepEntries = ArrayList<BarEntry>()

    private val _lastValue by lazy {MutableLiveData("0:0")}
    var lastValue: LiveData<String> = _lastValue

    private val _fetchingSleepDataState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingSleepDataState: LiveData<FetchingState> = _fetchingSleepDataState

    fun getSleepData(context: Context) {
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url = "http://localhost:5000/api/Gateway/GetSleephoursForWeek/9"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("sleepHours")
                    if(value.toString() == "null"){
                        weeklySleepEntries.add(BarEntry(i.toFloat(), 0f))
                    }
                    else {
                        weeklySleepEntries.add(BarEntry(i.toFloat(), (value as Double).toFloat()))
                    }
                }
                _fetchingSleepDataState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingSleepDataState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)

    }

    fun updateLastValue(sleepHours: String, time: String){
        _lastValue.value = sleepHours
    }
}