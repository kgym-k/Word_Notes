package human.johnson.android.wordnote.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.onboarding.screens.FirstScreenFragment
import human.johnson.android.wordnote.onboarding.screens.SecondScreenFragment
import human.johnson.android.wordnote.onboarding.screens.ThirdScreenFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreenFragment(),
            SecondScreenFragment(),
            ThirdScreenFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager_tutorial.adapter = adapter

        return view
    }

    override fun onStop() {
        super.onStop()

        // hide status bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}