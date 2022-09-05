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
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import elfak.mosis.health.R
import elfak.mosis.health.ui.sleep.MyXAxisFormatter
import elfak.mosis.health.ui.user.model.UserViewModel
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.avg_pulse
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.max_pulse

class HeartRateWeekFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val heartRateViewModel: HeartRateViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart_rate_week, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<LineChart>(R.id.bpm_week_chart)
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.xAxis.valueFormatter = MyXAxisFormatter()
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setBorderWidth(5f)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 6f
        chart.xAxis.gridLineWidth = 1f
        userViewModel.currentUser?.let { heartRateViewModel.getWeeklyData(view.context, it._id) }

        val fetchingStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val dataEntries = heartRateViewModel.weeklyEntries

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

        heartRateViewModel.fetchingWeeklyDataState.observe(viewLifecycleOwner, fetchingStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        heartRateViewModel.fetchingWeeklyDataState.removeObservers(viewLifecycleOwner)
    }
}