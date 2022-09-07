package elfak.mosis.health.ui.device

import android.bluetooth.le.ScanResult
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentBluetoothleBinding

import elfak.mosis.health.ui.device.placeholder.PlaceholderContent.PlaceholderItem


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBleDeviceRecyclerViewAdapter(
    private val items: List<ScanResult>,
    private val onClickListener: ((device: ScanResult) -> Unit)
) : RecyclerView.Adapter<MyBleDeviceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentBluetoothleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClickListener
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.deviceName.text = items[position].device.name
        holder.macAddress.text = items[position].device.address
        holder.signalStrength.text = "${items[position].rssi} dBm"
        holder.currentDevice = item

    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: FragmentBluetoothleBinding, onClickListener: ((device: ScanResult) -> Unit)) :
        RecyclerView.ViewHolder(binding.root) {
        val deviceName: TextView = binding.deviceName
        val macAddress: TextView = binding.macAddress
        val signalStrength: TextView = binding.signalStrength
        var currentDevice: ScanResult? = null

        init {
            binding.root.setOnClickListener {
                currentDevice?.let { onClickListener(it) }
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + macAddress.text + "'"
        }
    }

}