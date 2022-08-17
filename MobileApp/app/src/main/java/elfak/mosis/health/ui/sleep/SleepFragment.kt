package elfak.mosis.health.ui.sleep

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import elfak.mosis.health.R
import java.time.LocalDate

class SleepFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<BarChart>(R.id.bar_chart)

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


        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 8f))
        entries.add(BarEntry(1f, 7.5f))
        entries.add(BarEntry(2f, 8.3f))
        entries.add(BarEntry(3f, 4.2f))
        entries.add(BarEntry(6f, 6.3f))

        val dataSet = BarDataSet(entries, "Label")
        dataSet.color = Color.parseColor("#efdd32")
        dataSet.valueTextColor = Color.parseColor("#dd987a")

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f
        chart.data = barData
        chart.setFitBars(true)
        chart.invalidate()

    }

    fun setChartDate() {

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
            days.add(dayName)
        }
        days.add(today.dayOfWeek.toString())
    }
}

class MyYAxisFormatter : ValueFormatter() {
    private val values = arrayOf("0hr", "1hr", "2hr", "3hr", "4hr", "5hr", "6hr", "7hr", "8hr", "9hr", "10hr", "11hr", "12hr")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return values.getOrNull(value.toInt()) ?: value.toString()
    }
}