package elfak.mosis.health.ui.sleep

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentBloodPressureDayBinding
import elfak.mosis.health.databinding.FragmentSleepBinding
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.user.model.UserViewModel
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class SleepFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val sleepViewModel: SleepViewModel by activityViewModels()
    private var _binding: FragmentSleepBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTodayHours.text = sleepViewModel.lastValue.value

        val chart = view.findViewById<BarChart>(R.id.bar_chart)
        userViewModel.currentUser?.let { sleepViewModel.getSleepData(view.context, it._id) }

        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = MyXAxisFormatter()
        chart.xAxis.setDrawAxisLine(true)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisLeft.valueFormatter = MyYAxisFormatter()
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setLabelCount(7, false)
        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 12f
        chart.axisLeft.gridLineWidth = 2f
        chart.xAxis.axisMinimum = -0.5f
        chart.xAxis.axisMaximum = 6.5f
        chart.xAxis.gridLineWidth = 1f

        val fetchingStateObserver =  Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val dataEntries = sleepViewModel.weeklySleepEntries

                val dataSet = BarDataSet(dataEntries, "Label")
                dataSet.color = Color.parseColor("#efdd32")
                dataSet.valueTextColor = ContextCompat.getColor(view.context, R.color.black)
                dataSet.valueFormatter = SleepHoursFormatter()
                dataSet.valueTextSize = 10f

                val barData = BarData(dataSet)
                barData.barWidth = 0.9f
                chart.data = barData
                chart.setFitBars(true)
                chart.invalidate()
            }
            if(state is FetchingState.FetchingError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        sleepViewModel.fetchingSleepDataState.observe(viewLifecycleOwner, fetchingStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        sleepViewModel.fetchingSleepDataState.removeObservers(viewLifecycleOwner)
    }
}

class MyXAxisFormatter : ValueFormatter() {
    init {
        setDays()
    }
    private lateinit var days: MutableList<String>
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
    private fun setDays(){
        days = mutableListOf()
        val today = LocalDate.now()
        for (i in 6 downTo 1) {
            val day = today.minusDays(i.toLong())
            val dayName = day.dayOfWeek.toString()
            days.add(dayName.substring(0, 3))
        }
        days.add(today.dayOfWeek.toString().substring(0, 3))
    }
}

class MyYAxisFormatter : ValueFormatter() {
    private val values = arrayOf("0hr", "1hr", "2hr", "3hr", "4hr", "5hr", "6hr", "7hr", "8hr", "9hr", "10hr", "11hr", "12hr")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return values.getOrNull(value.toInt()) ?: value.toString()
    }
}

class SleepHoursFormatter(): ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        return if(barEntry?.y != null) {
            val minutes = barEntry.y * 60
            val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
            val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
            Log.i("BAR_VAL", hours.toString())
            Log.i("BAR_VAL", remainMinutes.toString())
            String.format("%02d:%02d", hours, remainMinutes.toInt())
        } else {
            ""
        }
    }
}