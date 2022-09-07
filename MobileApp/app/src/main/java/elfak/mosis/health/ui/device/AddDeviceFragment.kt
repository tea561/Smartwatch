package elfak.mosis.health.ui.device

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentAddDeviceBinding

class AddDeviceFragment : Fragment() {

    private val devicesViewModel: DeviceViewModel by activityViewModels()

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var _binding: FragmentAddDeviceBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_ENABLE_BT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDeviceBinding.inflate(inflater, container, false)

        val bluetoothManager: BluetoothManager? = ContextCompat.getSystemService(requireContext(), BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager!!.adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = binding.listViewConnectedDevices
        var listAdapter: BTAdapter
        devicesViewModel.connectedDevices.observe(viewLifecycleOwner){ devices ->

            listAdapter = BTAdapter(view.context, devices)
            listView.adapter = listAdapter
        }


        checkBTState()

        binding.buttonFind.setOnClickListener {
            if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled)
            {
                checkBTState()
            }
            else {
                findNavController().navigate(R.id.action_AddNewDeviceFragment_to_BleFragment)
            }
        }
    }

    private fun requiredPermissions(): Array<String>{
        val sdkVersion: Int = requireActivity().applicationInfo.targetSdkVersion
        //android 12
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && sdkVersion >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE)
        }
        //android 9 and lower
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && sdkVersion <= Build.VERSION_CODES.P){
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkBTState(){
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(requireContext(), "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        }
        else {
            if (!bluetoothAdapter.isEnabled) {
                Toast.makeText(requireContext(), "You need to enable Bluetooth.", Toast.LENGTH_SHORT)
                    .show()
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                try {
                    activity?.startActivityFromFragment(this, enableBtIntent, REQUEST_ENABLE_BT)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }
            } else {
                val permission: String = requiredPermissions()[0]
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(requireActivity(), requiredPermissions(), 1)
                    return
                }
                if (bluetoothAdapter.isDiscovering) {
                    Toast.makeText(
                        requireContext(),
                        "Device discovering process...",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Bluetooth enabled.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Bluetooth enabled.", Toast.LENGTH_SHORT).show()
        }
    }

}