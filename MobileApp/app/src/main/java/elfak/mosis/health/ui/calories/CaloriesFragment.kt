package elfak.mosis.health.ui.calories

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentBloodPressureBinding
import elfak.mosis.health.databinding.FragmentCaloriesBinding
import elfak.mosis.health.ui.bloodpressure.BloodPressureViewModel
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.sleep.MyXAxisFormatter
import elfak.mosis.health.ui.sleep.MyYAxisFormatter
import elfak.mosis.health.ui.user.model.UserViewModel


class CaloriesFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val caloriesViewModel: CaloriesViewModel by activityViewModels()

    private var _binding: FragmentCaloriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTodayCalories.text = "${caloriesViewModel.lastValue.value.toString()}kcal"


        val chart = view.findViewById<BarChart>(R.id.bar_chart)
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = MyXAxisFormatter()
        chart.xAxis.setDrawAxisLine(true)
        chart.axisLeft.setDrawAxisLine(false)
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setLabelCount(7, false)
        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        chart.axisLeft.axisMinimum = 0f
        chart.xAxis.axisMinimum = -0.5f
        chart.xAxis.axisMaximum = 6.5f
        chart.xAxis.gridLineWidth = 1f
        userViewModel.currentUser?.let { caloriesViewModel.getCalories(view.context, it._id) }

        val fetchingCaloriesStateObserver = Observer<FetchingState> {state ->
            if(state == FetchingState.Success) {
                val weekEntries = caloriesViewModel.weeklyEntries
                var dataSet = BarDataSet(weekEntries, "Label")

                dataSet.color = ContextCompat.getColor(view.context, R.color.green_light)
                dataSet.valueTextSize = 12f
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

        caloriesViewModel.fetchingCaloriesState.observe(viewLifecycleOwner, fetchingCaloriesStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        caloriesViewModel.fetchingCaloriesState.removeObservers(viewLifecycleOwner)
    }
}