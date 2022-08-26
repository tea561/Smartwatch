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
import elfak.mosis.health.databinding.FragmentBloodPressureDayBinding
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.user.model.UserViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class BloodPressureDayFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val bloodPressureViewModel: BloodPressureViewModel by activityViewModels()
    private var _binding: FragmentBloodPressureDayBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBloodPressureDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewDateTime = binding.textViewDateTime
        textViewDateTime.text = bloodPressureViewModel.lastTime.value!!.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
        binding.textViewDiasValue.text = bloodPressureViewModel.lastDiasValue.value.toString()
        binding.textViewSysValue.text = bloodPressureViewModel.lastSysValue.value.toString()

        val chart = view.findViewById<LineChart>(R.id.blood_pressure_day_chart)

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
        userViewModel.currentUser?.let { bloodPressureViewModel.getDailySysData(view.context, it._id) }

        var sysEntries = ArrayList<Entry>()
        var dataSetSys: LineDataSet = LineDataSet(sysEntries, "sys")


        val fetchingSysDataStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                sysEntries = bloodPressureViewModel.dailySysEntries

                dataSetSys = LineDataSet(sysEntries, "sys")
                dataSetSys.color = ContextCompat.getColor(view.context, R.color.purple)
                dataSetSys.lineWidth = 2f
                dataSetSys.setDrawValues(false)
                dataSetSys.circleRadius = 4f
                dataSetSys.setCircleColor(dataSetSys.color)
                dataSetSys.setDrawCircleHole(false)

                userViewModel.currentUser?.let { bloodPressureViewModel.getDailyDiasData(view.context, it._id) }
            }
            if(state is FetchingState.FetchingError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        bloodPressureViewModel.fetchingDailySysState.observe(viewLifecycleOwner, fetchingSysDataStateObserver)

        val fetchingDiasDataStateObserver = Observer<FetchingState> { state ->
            if(state == FetchingState.Success) {
                val dataEntries = bloodPressureViewModel.dailyDiasEntries

                val dataSetDias: LineDataSet = LineDataSet(dataEntries, "dias")
                dataSetDias.color = ContextCompat.getColor(view.context,R.color.turquoise)
                dataSetDias.lineWidth = 2f
                dataSetDias.circleRadius = 4f
                dataSetDias.setCircleColor(dataSetDias.color)
                dataSetDias.setDrawValues(false)
                dataSetDias.setDrawCircleHole(false)

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

        bloodPressureViewModel.fetchingDailyDiasState.observe(viewLifecycleOwner, fetchingDiasDataStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        bloodPressureViewModel.fetchingDailySysState.removeObservers(viewLifecycleOwner)
        bloodPressureViewModel.fetchingDailyDiasState.removeObservers(viewLifecycleOwner)
    }
}