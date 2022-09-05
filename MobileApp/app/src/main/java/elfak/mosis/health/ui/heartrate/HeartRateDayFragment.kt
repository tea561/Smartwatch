package elfak.mosis.health.ui.heartrate

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import elfak.mosis.health.R
import elfak.mosis.health.ui.user.model.UserViewModel
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.avg_pulse
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.max_pulse

class HeartRateDayFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private val heartRateViewModel:  HeartRateViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart_rate_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = SharedPreferencesHelper.customPreference(view.context, "First time")
        val avgVal = view.findViewById<TextView>(R.id.textViewDailyAvgVal)
        avgVal.text = prefs.avg_pulse.toString()
        val maxVal = view.findViewById<TextView>(R.id.textViewDailyMaxVal)
        maxVal.text = prefs.max_pulse.toString()


        val chart = view.findViewById<LineChart>(R.id.bpm_day_chart)
        userViewModel.currentUser?.let { heartRateViewModel.getDailyData(view.context, it._id) }

        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setBorderWidth(5f)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 24f
        chart.xAxis.gridLineWidth = 1f

        val fetchingStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val dataEntries = heartRateViewModel.dailyEntries

                val dataSet: LineDataSet = LineDataSet(dataEntries, "label")
                dataSet.color = Color.parseColor("#56CFE1")

                dataSet.lineWidth = 2f
                dataSet.setDrawValues(false)
                dataSet.setCircleColor(Color.parseColor("#56CFE1"))

                dataSet.setDrawCircleHole(false)
                dataSet.circleRadius = 5f
                dataSet.setDrawFilled(true)
                dataSet.fillDrawable = ContextCompat.getDrawable(view.context, R.drawable.gradient)

                val lineData: LineData = LineData(dataSet)
                chart.data = lineData
                chart.invalidate()
            }
            if(state is FetchingState.FetchingError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        heartRateViewModel.fetchingDailyDataState.observe(viewLifecycleOwner, fetchingStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        heartRateViewModel.fetchingDailyDataState.removeObservers(viewLifecycleOwner)
    }
}