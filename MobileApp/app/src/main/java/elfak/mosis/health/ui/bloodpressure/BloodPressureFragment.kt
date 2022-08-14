package elfak.mosis.health.ui.bloodpressure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentBloodPressureBinding
import elfak.mosis.health.databinding.FragmentHeartRateBinding
import elfak.mosis.health.ui.heartrate.HeartRateAdapter

class BloodPressureFragment : Fragment() {

    private var _binding: FragmentBloodPressureBinding? = null
    private val binding get() = _binding!!

    private lateinit var bloodPressureAdapter: BloodPressureAdapter
    private lateinit var viewPager2: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBloodPressureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bloodPressureAdapter= BloodPressureAdapter(this)
        viewPager2 = binding.pager
        viewPager2.adapter = bloodPressureAdapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = bloodPressureAdapter.fragmentNames[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}