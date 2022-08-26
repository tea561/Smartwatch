package elfak.mosis.health.ui.calories

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
import elfak.mosis.health.ui.heartrate.FetchingState
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class CaloriesViewModel: ViewModel(){
    var weeklyEntries = ArrayList<BarEntry>()
    private val _lastValue by lazy { MutableLiveData(0f) }
    var lastValue: LiveData<Float> = _lastValue

    private val _fetchingCaloriesState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingCaloriesState: LiveData<FetchingState> = _fetchingCaloriesState

    fun getCalories(context: Context, _id: Int) {
        _fetchingCaloriesState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetDailySumForWeek/calories/$_id"
        val url = "http://localhost:5000/api/Gateway/GetDailySumForWeek/calories/$_id"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("value")
                    if(value.toString() == "null"){
                        weeklyEntries.add(BarEntry(i.toFloat(), 0f))
                    }
                    else {
                        var y: Float = 0f
                        Log.i("TYPE", value::class.java.name)
                        if(value is Double){
                            y = value.toFloat()
                        }
                        else if (value is Int){
                            y = value.toFloat()
                        }
                        weeklyEntries.add(BarEntry(i.toFloat(), y))
                    }
                }
                _fetchingCaloriesState.value = FetchingState.Success
            },
            { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingCaloriesState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)

    }

    fun updateLastValue(calories: Float, time: String){
        _lastValue.value = calories
    }
}