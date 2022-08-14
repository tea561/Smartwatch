package elfak.mosis.health.ui.bloodpressure

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import elfak.mosis.health.R
import elfak.mosis.health.ui.sleep.MyXAxisFormatter

class BloodPressureWeekFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_pressure_week, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 110f))
        entries.add(Entry(1f, 100f))
        entries.add(Entry(2f, 125f))
        entries.add(Entry(3f, 110f))
        entries.add(Entry(4f, 108f))
        entries.add(Entry(5f, 117f))
        entries.add(Entry(6f, 115f))

        val entries2 = ArrayList<Entry>()
        entries2.add(Entry(0f, 80f))
        entries2.add(Entry(1f, 75f))
        entries2.add(Entry(2f, 85f))
        entries2.add(Entry(3f, 81f))
        entries2.add(Entry(4f, 78f))
        entries2.add(Entry(5f, 83f))
        entries2.add(Entry(6f, 88f))

        val dataSetSys: LineDataSet = LineDataSet(entries, "sys")
        dataSetSys.color = ContextCompat.getColor(view.context, R.color.purple)
        dataSetSys.lineWidth = 2f
        dataSetSys.circleRadius = 4f
        dataSetSys.setCircleColor(dataSetSys.color)
        dataSetSys.valueTextSize = 10f
        dataSetSys.setDrawCircleHole(false)

        val dataSetDias: LineDataSet = LineDataSet(entries2, "dias")
        dataSetSys.color = ContextCompat.getColor(view.context,R.color.turquoise)
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
}