package elfak.mosis.health.ui.bloodpressure

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import elfak.mosis.health.ui.heartrate.HeartRateDayFragment
import elfak.mosis.health.ui.heartrate.HeartRateWeekFragment

class BloodPressureAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    public val fragmentNames = listOf<String>("Day", "Week")
    public val fragments = listOf<Fragment>(BloodPressureDayFragment(), BloodPressureWeekFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}