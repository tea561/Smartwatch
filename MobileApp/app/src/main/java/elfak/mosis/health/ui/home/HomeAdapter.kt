package elfak.mosis.health.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.health.R


class HomeAdapter(context: Context) : RecyclerView.Adapter<HomeAdapter.MyAdapter>() {
    var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyAdapter(view)
    }

    override fun onBindViewHolder(holder: MyAdapter, position: Int) {
        if (position == 0) {
            holder.image.setImageResource(R.drawable.blooddonalback)
            holder.image1.setImageResource(R.drawable.blooddonar)
            holder.back.setBackgroundColor(Color.parseColor("#E6E53935"))
            holder.text.text = "Donate blood every year"
        }
        if (position == 1) {
            holder.image.setImageResource(R.drawable.volunteeringback)
            holder.image1.setImageResource(R.drawable.volunteer)
            holder.back.setBackgroundColor(Color.parseColor("#F236883A"))
            holder.text.text = "Large local volunteers"
        }
        if (position == 2) {
            holder.image.setImageResource(R.drawable.doorvisitback)
            holder.image1.setImageResource(R.drawable.doortodoorvisit)
            holder.back.setBackgroundColor(Color.parseColor("#F2AF4576"))
            holder.text.text = "Everyday door to door visit"
        }
        if (position == 3) {
            holder.image.setImageResource(R.drawable.localeventback)
            holder.image1.setImageResource(R.drawable.localeventorg)
            holder.back.setBackgroundColor(Color.parseColor("#F2EEAA45"))
            holder.text.text = "Organize local events"
        }
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