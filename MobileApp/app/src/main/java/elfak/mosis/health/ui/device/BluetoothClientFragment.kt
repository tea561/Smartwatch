package elfak.mosis.health.ui.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import elfak.mosis.health.R
import elfak.mosis.health.ui.user.model.UserViewModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothClientFragment : Fragment() {

    private val devicesViewModel: DeviceViewModel by activityViewModels()

    private val REQUEST_ENABLE_BT = 1
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothDevices =  mutableListOf<BluetoothDevice>()
    private lateinit var listAdapter: BTAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bluetooth_client, container, false)

        val bluetoothManager: BluetoothManager? = ContextCompat.getSystemService(requireContext(), BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager!!.adapter

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val buttonScan: Button = requireView().findViewById(R.id.buttonScan)
        val pairedDevicesList: ListView = requireView().findViewById(R.id.listViewPairedDevices)
        listAdapter = BTAdapter(view.context, bluetoothDevices)
        pairedDevicesList.adapter = listAdapter
        pairedDevicesList.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var BTDevice: BluetoothDevice = p0?.adapter?.getItem(p2) as BluetoothDevice
                Toast.makeText(view.context, BTDevice.toString(), Toast.LENGTH_SHORT).show()
                val connectThread = ConnectThread(BTDevice)
                connectThread.start()
            }
        })


        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(receiver,filter)

        buttonScan.setOnClickListener {
            if(bluetoothAdapter != null && bluetoothAdapter.isEnabled){
//                val pairedDevices = bluetoothAdapter.bondedDevices
//                pairedDevices?.forEach { device ->
//                    val deviceName = device.name
//                    val deviceHardwareAddress = device.address // MAC address
//                    bluetoothDevices.add(device)
//                }
                //bluetoothDevices?.clear()
                listAdapter?.clear()
                bluetoothAdapter.startDiscovery()

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                activity?.registerReceiver(receiver,filter)
            }
            else
            {
                Log.e("BluetoothClient", "Bluetooth disabled")
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action!!) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    devicesViewModel.addDevice(device)
                    listAdapter.add(device)
                    listAdapter.notifyDataSetChanged()
                    bluetoothDevices.add(device)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        activity?.unregisterReceiver(receiver)
        bluetoothAdapter.cancelDiscovery()
        super.onDestroy()

    }


    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.fromString("505961ad-0e61-4de4-a0d9-313c06572d09"))
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        private fun manageMyConnectedSocket(socket: BluetoothSocket) {
            val connectionThread = ConnectedThread(socket)
            connectionThread.start()
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }

        private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread()
        {
            private val mmInStream: InputStream = mmSocket.inputStream
            private val mmOutStream: OutputStream = mmSocket.outputStream
            private val mmBuffer: ByteArray = ByteArray(28) // mmBuffer store for the stream


            override fun run() {
                var numBytes: Int // bytes returned from read()
                val uid: String = "connect"

                //send uid
                try{
                    mmOutStream.write(uid.encodeToByteArray())
                } catch (e: IOException){
                    Log.e(TAG, "Error occurred when sending data", e)
                }

            }

            fun cancel() {
                try{
                    mmSocket.close()
                } catch (e: IOException){
                    Log.e(TAG, "Could not close the connect socket", e)
                }
            }

        }
    }



}