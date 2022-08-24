package elfak.mosis.health.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentHomeBinding
import elfak.mosis.health.ui.bloodpressure.BloodPressureViewModel
import elfak.mosis.health.ui.heartrate.HeartRateViewModel
import elfak.mosis.health.ui.sleep.SleepViewModel
import elfak.mosis.health.ui.stepcounter.StepCounterService
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bloodPressureViewModel: BloodPressureViewModel by activityViewModels()
    private val heartRateViewModel: HeartRateViewModel by activityViewModels()
    private val sleepViewModel: SleepViewModel by activityViewModels()


    private var values: MutableList<String> = mutableListOf("0", "0", "0", "0")

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val homeAdapter = HomeAdapter(requireContext(), values)
        recyclerView.adapter = homeAdapter

        //http
        val queue = Volley.newRequestQueue(view.context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/9"
        val url2 = "http://localhost:5000/api/Gateway/GetAllParameters/9"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url2, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                val sys = response["sys"]
                val pulse = response["pulse"]
                val dias = response["dias"]
                val time = response["timestampVitals"]
                val timestampSleepData = response["timestampSleepData"]
                val sleepHours = response["sleepHours"]
                val minutes = sleepHours as Double * 60
                val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
                val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
                val sleepHoursString = String.format("%02d h %02d min", hours, remainMinutes.toInt())

                bloodPressureViewModel.updateLastValues(sys as Int, dias as Int, time as Long)
                heartRateViewModel.updateLastValues(pulse as Int, time.toLong())
                sleepViewModel.updateLastValue(sleepHoursString, timestampSleepData.toString())

                values[0] = pulse.toString()
                values[1] = sleepHoursString
                values[2] = "${sys.toString()}/${dias.toString()}"
                homeAdapter.notifyItemChanged(0)
                homeAdapter.notifyItemChanged(1)
                homeAdapter.notifyItemChanged(2)

            },
            Response.ErrorListener { error ->
                Log.i("HTTP", "Error: ${error.toString()}")
            }
        )

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)

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

        val listener =
            OnSharedPreferenceChangeListener { prefs, key ->
                binding.progressBar.progress = prefs.stepCount / 100
                binding.textViewProgress.text = prefs.stepCount.toString()
            }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        binding.progressBar.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_StepsFragment)
        }
        binding.progressBar.progress = prefs.stepCount / 100
        binding.textViewProgress.text = prefs.stepCount.toString()
        binding.progressBar.max = 100

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

    fun sendGetRequest() {
        val url = URL("http://www.android.com/")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"

            Log.i("HTTP", "Url: $url")
            Log.i("HTTP", "Response code: $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while(inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response: $response")
            }
        }
    }

    fun updateSteps() {
        val prefs = SharedPreferencesHelper.customPreference(requireContext(), "Step_data")
        binding.progressBar.progress = prefs.stepCount
    }
}