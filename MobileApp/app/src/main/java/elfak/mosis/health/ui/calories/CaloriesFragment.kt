package elfak.mosis.health.ui.calories

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import elfak.mosis.health.R
import elfak.mosis.health.ui.sleep.MyXAxisFormatter
import elfak.mosis.health.ui.sleep.MyYAxisFormatter


class CaloriesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 234f))
        entries.add(BarEntry(1f, 124f))
        entries.add(BarEntry(2f, 542f))
        entries.add(BarEntry(3f, 331f))
        entries.add(BarEntry(6f, 329f))

        val dataSet = BarDataSet(entries, "Label")
        dataSet.color = ContextCompat.getColor(view.context, R.color.green_light)
        dataSet.valueTextSize = 12f
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f
        chart.data = barData
        chart.setFitBars(true)
        chart.invalidate()

    }
}