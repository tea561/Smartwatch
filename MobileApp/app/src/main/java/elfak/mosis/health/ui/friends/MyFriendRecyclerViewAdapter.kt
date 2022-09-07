package elfak.mosis.health.ui.friends

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentFriendsBinding
import elfak.mosis.health.ui.friends.placeholder.PlaceholderContent.PlaceholderItem
import elfak.mosis.health.ui.user.data.User
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class MyFriendRecyclerViewAdapter(private val onClick: (User) -> Unit,
                                  private var values: List<User> = emptyList()
) : RecyclerView.Adapter<MyFriendRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentFriendsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    fun setData(newData:List<User>){
        values = newData.sortedByDescending { it.rank }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.currentFriend = item
        holder.idView.text = "${item.username}"
        holder.contentView.text = item.username
        var calories = item.rank
        if(calories is Double)
            calories = (calories * 100.0).roundToInt() / 100.0
        holder.buttonRank.text = calories.toString()

        Glide.with(holder.itemView).load(item.imgSrc).centerCrop().into(holder.imageViewFriend)

        //set img
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null

//        executor.execute{
//            Log.i("IMG SRC", item.imgSrc.toString())
//            val imageUrl = item.imgSrc
//            try {
//                val `in` = java.net.URL(imageUrl).openStream()
//                image = BitmapFactory.decodeStream(`in`)
//
//                handler.post{
//                    holder.imageViewFriend.setImageBitmap(image)
//                }
//            }
//            catch(e: Exception){
//                e.printStackTrace()
//            }
//        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentFriendsBinding, val onClick: (User) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.friendName
        val contentView: TextView = binding.friendUsername
        val imageViewFriend: ImageView = binding.imageViewFriend
        val buttonRank: Button = binding.buttonRank
        var currentFriend: User? = null

        init {
            binding.root.setOnClickListener { currentFriend?.let { onClick(it) } }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}