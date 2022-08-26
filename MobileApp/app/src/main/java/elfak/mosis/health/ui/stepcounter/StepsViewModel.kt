package elfak.mosis.health.ui.stepcounter

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

class StepsViewModel : ViewModel(){
    var weeklyStepsEntries = ArrayList<BarEntry>()

    private val _lastValue by lazy { MutableLiveData(0) }
    var lastValue: LiveData<Int> = _lastValue

    private val _fetchingStepsDataState by lazy { MutableLiveData<FetchingState>(FetchingState.Idle) }
    val fetchingStepsDataState: LiveData<FetchingState> = _fetchingStepsDataState

    fun getStepsData(context: Context, _id: Int) {
        _fetchingStepsDataState.value = FetchingState.Idle
        //http
        val queue = Volley.newRequestQueue(context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetDailySumForWeek/steps/$_id"
        val url = "http://localhost:5000/api/Gateway/GetDailySumForWeek/steps/$_id"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                for(i in 0 until response.length()){
                    val element = response.getJSONObject(i)
                    Log.i("JSON", element.toString())
                    val value = element.get("value")
                    if(value.toString() == "null"){
                        weeklyStepsEntries.add(BarEntry(i.toFloat(), 0f))
                    }
                    else {
                        weeklyStepsEntries.add(BarEntry(i.toFloat(), (value as Int).toFloat()))
                    }
                }
                _fetchingStepsDataState.value = FetchingState.Success
            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
                _fetchingStepsDataState.value = FetchingState.FetchingError(error.toString())
            }
        )

        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(jsonArrayRequest)

    }

    fun updateLastValue(steps: Int, time: String){
        _lastValue.value = steps
    }
}