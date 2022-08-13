package elfak.mosis.health.ui.heartrate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentHeartRateBinding

class HeartRateFragment : Fragment() {

    private var _binding: FragmentHeartRateBinding? = null
    private val binding get() = _binding!!

    private lateinit var heartRateAdapter: HeartRateAdapter
    private lateinit var viewPager2: ViewPager2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeartRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heartRateAdapter = HeartRateAdapter(this)
        viewPager2 = binding.pager
        viewPager2.adapter = heartRateAdapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = heartRateAdapter.fragmentNames[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}