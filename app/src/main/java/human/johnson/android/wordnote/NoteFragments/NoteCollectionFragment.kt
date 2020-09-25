package human.johnson.android.wordnote.NoteFragments

import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.android.material.tabs.TabLayout
import human.johnson.android.wordnote.R
import kotlinx.android.synthetic.main.fragment_note_collection.view.*
import human.johnson.android.wordnote.MyApplication.Companion.currentId
import human.johnson.android.wordnote.MyApplication.Companion.star_flag
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note_star.*

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
        currentId = args.currentId

        tabLayout = view.tab_layout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.all)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.star)))

        if (star_flag) {
            tabLayout.getTabAt(1)?.select()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NoteStarFragment()).commit()
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
                if (tab?.position == 0) {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NoteFragment()).commit()
                } else {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NoteStarFragment()).commit()
                }
            }
        })
    }
}