package human.johnson.android.wordnote.NoteFragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import human.johnson.android.wordnote.MyApplication
import human.johnson.android.wordnote.MyApplication.Companion.check_flag
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.MyApplication.Companion.currentId
import human.johnson.android.wordnote.MyApplication.Companion.star_flag
import kotlinx.android.synthetic.main.fragment_note_collection.view.*

class NoteCollectionFragment : Fragment() {
    private val args by navArgs<NoteCollectionFragmentArgs>()
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_collection, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MyApplication.currentId = args.currentId

        tabLayout = view.tab_layout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.all)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.star)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.check)))

        if (star_flag) {
            tabLayout.getTabAt(1)?.select()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NoteStarFragment()).commit()
        }
        else if (check_flag) {
            tabLayout.getTabAt(2)?.select()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NoteCheckFragment()).commit()
        }
        else {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NoteFragment()).commit()
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NoteFragment()).commit()
                    1 -> childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NoteStarFragment()).commit()
                    2 -> childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NoteCheckFragment()).commit()
                }
            }
        })
    }
}