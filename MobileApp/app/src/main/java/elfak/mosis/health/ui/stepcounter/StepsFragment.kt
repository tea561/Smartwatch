package elfak.mosis.health.ui.stepcounter

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
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentSleepBinding
import elfak.mosis.health.databinding.FragmentStepsBinding
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.sleep.MyXAxisFormatter
import elfak.mosis.health.ui.sleep.MyYAxisFormatter
import elfak.mosis.health.ui.sleep.SleepHoursFormatter
import elfak.mosis.health.ui.sleep.SleepViewModel
import elfak.mosis.health.ui.user.model.UserViewModel

class StepsFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val stepsViewModel: StepsViewModel by activityViewModels()
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStepsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTodaySteps.text = stepsViewModel.lastValue.value.toString()

        val chart = view.findViewById<BarChart>(R.id.bar_chart)
        userViewModel.currentUser?.let { stepsViewModel.getStepsData(view.context, it._id) }

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

        val fetchingStateObserver =  Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val dataEntries = stepsViewModel.weeklyStepsEntries

                val dataSet = BarDataSet(dataEntries, "Label")
                dataSet.color = Color.parseColor("#efdd32")
                dataSet.valueTextColor = ContextCompat.getColor(view.context, R.color.black)
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

        stepsViewModel.fetchingStepsDataState.observe(viewLifecycleOwner, fetchingStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        stepsViewModel.fetchingStepsDataState.removeObservers(viewLifecycleOwner)
    }
}