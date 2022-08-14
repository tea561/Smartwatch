package elfak.mosis.health.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentHomeBinding
import elfak.mosis.health.ui.stepcounter.StepCounterService
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.registerReceiver(stepCountReceiver, IntentFilter("broadcast_steps"))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = HomeAdapter(requireContext())

        if (!StepCounterService.checkPermission(view.context)) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1
            )
        }

        if (!checkStepCountServiceRunning()) {
            val serviceIntent = Intent(view.context, StepCounterService().javaClass)
            requireActivity().startService(serviceIntent)
        }

//        val circularProgressBar = view.findViewById<CircularProgressBar>(R.id.circularProgressBar)
//
//        circularProgressBar.apply {
//            setProgressWithAnimation(35f, 1000)
//
//            progressMax = 100f
//
//        }


        val prefs = SharedPreferencesHelper.customPreference(view.context, "Step_data")


        binding.progressBar.progress = prefs.stepCount
        binding.textViewProgress.text = prefs.stepCount.toString()
        binding.progressBar.max = 10000

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(stepCountReceiver)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkStepCountServiceRunning(): Boolean {
        val activityManager =
            requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (StepCounterService::class.simpleName == service.service.className) {
                Log.e("MAP", "Step Count Service already running.")
                return true
            }
        }
        return false
    }

    private val stepCountReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                if (bundle.containsKey("steps")) {
                    val stepCount = bundle.getString("steps")
                    if (stepCount != null) {
                        binding.progressBar.progress = stepCount.toInt()
                        binding.textViewProgress.text = stepCount.toString()
                    }
                    Log.e("MainActivity--", "token--$stepCount")

                }
            }
        }

    }
}