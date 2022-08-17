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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.Utils.init
import elfak.mosis.health.R


class HomeAdapter(context: Context, values: List<String>) : RecyclerView.Adapter<HomeAdapter.MyAdapter>() {
    var context: Context
    private var images: List<Int> = emptyList()
    private var secondImages: List<Int> = emptyList()
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
        images = listOf(R.drawable.heartrate2, R.drawable.zzz, R.drawable.arm, R.drawable.exercise )
        secondImages = listOf(R.drawable.solidheart, R.drawable.baseline_nightlight_round_24, R.drawable.bloodpressure,
            R.drawable.fire)
        colors = listOf(R.color.purple, R.color.turquoise, R.color.blue, R.color.purple2)
        actions = listOf(R.id.action_HomeFragment_to_HeartRateFragment, R.id.action_HomeFragment_to_SleepFragment,
        R.id.action_HomeFragment_to_BloodPressureFragment, R.id.action_HomeFragment_to_CaloriesFragment)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) {

        holder.image.setImageResource(images[position])
        holder.image1.setImageResource(secondImages[position])
        holder.back.setBackgroundColor(colors[position])
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
        var image1: ImageView
        var text: TextView
        var back: RelativeLayout

        init {
            image = itemView.findViewById(R.id.image)
            image1 = itemView.findViewById(R.id.image1)
            text = itemView.findViewById(R.id.text)
            back = itemView.findViewById(R.id.back)
        }
    }

    init {
        this.context = context
    }
}