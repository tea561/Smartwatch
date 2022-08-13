package elfak.mosis.health.ui.heartrate

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import elfak.mosis.health.R

class HeartRateWeekFragment : Fragment() {

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

        val lineData: LineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()




    }

}