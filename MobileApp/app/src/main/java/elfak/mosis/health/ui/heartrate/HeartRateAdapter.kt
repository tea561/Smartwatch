package elfak.mosis.health.ui.heartrate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HeartRateAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    public val fragmentNames = listOf<String>("Day", "Week")
    public val fragments = listOf<Fragment>(HeartRateDayFragment(), HeartRateWeekFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}