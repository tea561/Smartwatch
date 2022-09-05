package elfak.mosis.health.ui.home

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.Utils.init
import elfak.mosis.health.R


class HomeAdapter(context: Context, values: List<String>) : RecyclerView.Adapter<HomeAdapter.MyAdapter>() {
    var context: Context
    private var images: List<Int> = emptyList()
    private var titles: List<String> = emptyList()
    private var colors: List<Int> = emptyList()
    private var actions: List<Int> = emptyList()
    var parameters = values

    init {
        setData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyAdapter(view)
    }

    private fun setData()
    {
        images = listOf(R.drawable.bckg12, R.drawable.bckg6, R.drawable.bckg7, R.drawable.bckg5 )
        titles = listOf("Heart Rate", "Sleep Hours", "Blood Pressure", "Calories")
        colors = listOf(R.color.transparent_purple, R.color.transparent_turquoise, R.color.transparent_blue, R.color.transparent_purple2)
        actions = listOf(R.id.action_HomeFragment_to_HeartRateFragment, R.id.action_HomeFragment_to_SleepFragment,
        R.id.action_HomeFragment_to_BloodPressureFragment, R.id.action_HomeFragment_to_CaloriesFragment)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) {

        holder.image.setImageResource(images[position])
        holder.text1.text = titles[position]
        holder.back.setBackgroundColor(ContextCompat.getColor(context,colors[position]))
        Log.i("COLOR", colors[position].toString())
        holder.text.text = parameters[position]
        holder.back.setOnClickListener (
            Navigation.createNavigateOnClickListener(actions[position])
                )
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class MyAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var text1: TextView
        var text: TextView
        var back: RelativeLayout

        init {
            image = itemView.findViewById(R.id.image)
            text1 = itemView.findViewById(R.id.text1)
            text = itemView.findViewById(R.id.text)
            back = itemView.findViewById(R.id.back)
        }
    }

    init {
        this.context = context
    }
}