package elfak.mosis.health.ui.bloodpressure

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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentBloodPressureWeekBinding
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.sleep.MyXAxisFormatter
import elfak.mosis.health.ui.user.model.UserViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class BloodPressureWeekFragment : Fragment() {

    private val bloodPressureViewModel: BloodPressureViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var _binding: FragmentBloodPressureWeekBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBloodPressureWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewDateTime.text = bloodPressureViewModel.lastTime.value!!.format(DateTimeFormatter.ofLocalizedDateTime(
            FormatStyle.MEDIUM))
        binding.textViewDiasValue.text = bloodPressureViewModel.lastDiasValue.value.toString()
        binding.textViewSysValue.text = bloodPressureViewModel.lastSysValue.value.toString()

        val chart = view.findViewById<LineChart>(R.id.blood_pressure_week_chart)

        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.description.isEnabled = false
        chart.setBorderWidth(5f)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 6f
        chart.xAxis.gridLineWidth = 1f
        chart.xAxis.valueFormatter = MyXAxisFormatter()
        userViewModel.currentUser?.let { bloodPressureViewModel.getWeeklySysData(view.context, it._id) }

        var sysEntries = ArrayList<Entry>()
        var dataSetSys: LineDataSet = LineDataSet(sysEntries, "sys")

        val fetchingSysDataStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                sysEntries = bloodPressureViewModel.weeklySysEntries

                dataSetSys = LineDataSet(sysEntries, "sys")
                dataSetSys.color = ContextCompat.getColor(view.context, R.color.purple)
                dataSetSys.lineWidth = 2f
                dataSetSys.circleRadius = 4f
                dataSetSys.setCircleColor(dataSetSys.color)
                dataSetSys.valueTextSize = 10f
                dataSetSys.setDrawCircleHole(false)

                userViewModel.currentUser?.let { bloodPressureViewModel.getWeeklyDiasData(view.context, it._id) }
            }
            if(state is FetchingState.FetchingError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        bloodPressureViewModel.fetchingWeeklySysState.observe(viewLifecycleOwner, fetchingSysDataStateObserver)

        val fetchingDiasDataStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val diasEntries = bloodPressureViewModel.weeklyDiasEntries

                val dataSetDias: LineDataSet = LineDataSet(diasEntries, "dias")
                dataSetDias.color = ContextCompat.getColor(view.context,R.color.turquoise)
                dataSetDias.lineWidth = 2f
                dataSetDias.circleRadius = 4f
                dataSetDias.setCircleColor(dataSetDias.color)
                dataSetDias.setDrawCircleHole(false)
                dataSetDias.valueTextSize = 10f


                val dataSets = arrayListOf<ILineDataSet>()
                dataSets.add(dataSetSys)
                dataSets.add(dataSetDias)

                val lineData = LineData(dataSets)
                chart.data = lineData
                chart.invalidate()
            }

            if(state is FetchingState.FetchingError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        bloodPressureViewModel.fetchingWeeklyDiasState.observe(viewLifecycleOwner, fetchingDiasDataStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bloodPressureViewModel.fetchingWeeklySysState.removeObservers(viewLifecycleOwner)
        bloodPressureViewModel.fetchingWeeklyDiasState.removeObservers(viewLifecycleOwner)
    }
}