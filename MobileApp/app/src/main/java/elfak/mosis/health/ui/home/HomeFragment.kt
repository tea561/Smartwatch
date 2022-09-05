package elfak.mosis.health.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.*
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentHomeBinding
import elfak.mosis.health.ui.bloodpressure.BloodPressureViewModel
import elfak.mosis.health.ui.calories.CaloriesViewModel
import elfak.mosis.health.ui.heartrate.HeartRateViewModel
import elfak.mosis.health.ui.sleep.SleepViewModel
import elfak.mosis.health.ui.stepcounter.StepCounterService
import elfak.mosis.health.ui.user.model.UserViewModel
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.firstTime
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.stepCount
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val bloodPressureViewModel: BloodPressureViewModel by activityViewModels()
    private val heartRateViewModel: HeartRateViewModel by activityViewModels()
    private val sleepViewModel: SleepViewModel by activityViewModels()
    private val caloriesViewModel: CaloriesViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    private val listener =
        OnSharedPreferenceChangeListener { prefs, key ->
            Log.i("STEPS", "LISTENER")
            binding.progressBar.progress = prefs.stepCount / 100
            binding.textViewProgress.text = prefs.stepCount.toString()
        }


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
        val textUser = view.findViewById<TextView>(R.id.text2)
        textUser.text = "Hello, ${userViewModel.currentUser?.username}"

        //http
        val queue = Volley.newRequestQueue(view.context)
        //val url2 = "http://192.168.1.5:5000/api/Gateway/GetParameters/${userViewModel.currentUser?._id}"
        val url2 = "http://localhost:5000/api/Gateway/GetAllParameters/${userViewModel.currentUser?._id}"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url2, null,
            Response.Listener { response ->
                Log.i("HTTP", "Response: %s".format(response.toString()))
                val sys = response["sys"]
                val pulse = response["pulse"]
                val dias = response["dias"]
                val time = response["timestamp"]
                var calories = if(response["calories"] is Double) response["calories"] as Double else response["calories"] as Int
                if(calories is Double)
                    calories = (calories * 100.0).roundToInt() / 100.0
                //val steps = response["steps"]
                var sleepHours : Double = 0.0
                if (response["sleepHours"] is Double)
                    sleepHours = response["sleepHours"] as Double
                else if(response["sleepHours"] is Int)
                    (response["sleepHours"] as Int).toDouble()

                val minutes = sleepHours * 60.0
                val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
                val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
                val sleepHoursString = String.format("%02d h %02d min", hours, remainMinutes.toInt())

                bloodPressureViewModel.updateLastValues(sys as Int, dias as Int, time as Long)
                heartRateViewModel.updateLastValues(pulse as Int, time.toLong())
                sleepViewModel.updateLastValue(sleepHoursString, time.toString())
                caloriesViewModel.updateLastValue(calories.toFloat(), time.toString())

                values[0] = "${pulse.toString()} bpm"
                values[1] = sleepHoursString
                values[2] = "${sys.toString()}/${dias.toString()} mmHg"
                values[3] = "${calories.toString()} kcal"
                homeAdapter.notifyItemChanged(0)
                homeAdapter.notifyItemChanged(1)
                homeAdapter.notifyItemChanged(2)
                homeAdapter.notifyItemChanged(3)

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


        prefs = SharedPreferencesHelper.customPreference(view.context, "First time")
        //prefs.firstTime = true


        prefs.registerOnSharedPreferenceChangeListener(listener)

        binding.progressBar.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_StepsFragment)
        }
        binding.progressBar.progress = prefs.stepCount / 100
        binding.textViewProgress.text = prefs.stepCount.toString()
        binding.progressBar.max = 100

        val navView: NavigationView? = activity?.findViewById(R.id.nav_view) ?: null
        if(navView != null) {
            val headerView: View = navView.getHeaderView(0)
            val usernameHeader: TextView = headerView.findViewById(R.id.textViewUsername)

            val headerImgProfile: ImageView = headerView.findViewById(R.id.imageViewProfile)
            usernameHeader.text = "${userViewModel.currentUser!!.username} #${userViewModel.currentUser!!._id}"

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap? = null

            Glide.with(headerView).load(userViewModel.currentUser!!.imgSrc).into(headerImgProfile)
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(stepCountReceiver)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
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
            Log.i("STEPS", "Bundle")
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
        val prefs = SharedPreferencesHelper.customPreference(requireContext(), "First time")
        binding.progressBar.progress = prefs.stepCount
    }
}