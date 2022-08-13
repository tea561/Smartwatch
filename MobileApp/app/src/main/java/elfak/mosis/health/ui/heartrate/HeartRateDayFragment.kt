package elfak.mosis.health.ui.heartrate

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
import elfak.mosis.health.R

class HeartRateDayFragment : Fragment() {
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

        val chart = view.findViewById<LineChart>(R.id.bpm_day_chart)

        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.gridColor = Color.parseColor("#C0C0C0")
        chart.axisLeft.gridColor = Color.parseColor("#C0C0C0")
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setBorderWidth(5f)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 7f
        chart.xAxis.gridLineWidth = 1f

        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 110f))
        entries.add(Entry(1f, 100f))
        entries.add(Entry(2f, 75f))
        entries.add(Entry(3f, 80f))
        entries.add(Entry(4f, 95f))
        entries.add(Entry(5f, 78f))
        entries.add(Entry(6f, 95f))

        val dataSet: LineDataSet = LineDataSet(entries, "label")
        dataSet.color = Color.parseColor("#ff084a")
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false)
        dataSet.setCircleColor(Color.parseColor("#ff084a"))

        dataSet.setDrawCircleHole(false)
        dataSet.circleRadius = 5f
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = ContextCompat.getDrawable(view.context, R.drawable.gradient)

        val lineData: LineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }
}