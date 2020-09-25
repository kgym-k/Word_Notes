package human.johnson.android.wordnote.NoteFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import human.johnson.android.wordnote.MyApplication
import human.johnson.android.wordnote.R
import kotlinx.android.synthetic.main.fragment_note_star.view.*
import kotlinx.android.synthetic.main.fragment_note_collection.*
import human.johnson.android.wordnote.MyApplication.Companion.currentId
import human.johnson.android.wordnote.MyApplication.Companion.star_flag
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note_star.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_note.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_sort_note.view.*

class NoteStarFragment() : Fragment() {
    private lateinit var mNoteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        star_flag = true
        val view = inflater.inflate(R.layout.fragment_note_star, container, false)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        recyclerView = view.recyclerview_note_star
        adapter = NoteAdapter(mNoteViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        mNoteViewModel.setStar(true)
        mNoteViewModel.note.observe(viewLifecycleOwner, Observer { note ->
            adapter.setData(note)

            // visibility
            val note_imageView_card: ImageView = view.findViewById(R.id.note_imageView_star)
            val note_text_card: TextView = view.findViewById(R.id.note_textView_star)
            if (adapter.itemCount == 0) {
                note_imageView_card.visibility = View.VISIBLE
                note_text_card.visibility = View.VISIBLE
            }
            else {
                note_imageView_card.visibility = View.INVISIBLE
                note_text_card.visibility = View.INVISIBLE
            }
        })

        // fab
        view.fab_note_star.setOnClickListener {
            val action =
                NoteCollectionFragmentDirections.actionNoteCollectionFragmentToNoteEditFragment(
                    MyApplication.currentId
                )
            view.findNavController().navigate(action)
        }

        // scroll
        (parentFragment as NoteCollectionFragment).tab_layout
            .addOnTabSelectedListener((object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    recyclerView.smoothScrollToPosition(0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                }
            }))

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // hide fab
        val search = menu?.findItem(R.id.menu_search)
        search.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                fab_note_star.visibility = View.GONE
                menu.getItem(1).setVisible(false)
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                fab_note_star.visibility = View.VISIBLE
                menu.getItem(1).setVisible(true)
                return true
            }
        })

        // search
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean{
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var newText = "%" + newText + "%"
                mNoteViewModel.setStr(newText)
                mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), true)
                return false
            }
        })
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_options -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView: View = LayoutInflater.from(requireContext())
                    .inflate(R.layout.layout_bottom_sheet_note, requireView()
                        .findViewById(R.id.bottom_sheet_container))
                bottomSheetView.bottom_sort.setOnClickListener {
                    sortBotomm()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_flip_to_front.setOnClickListener {
                    flipToFront()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_flip_to_back.setOnClickListener {
                    flipToBack()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_delete.setOnClickListener {
                    deleteAllNote()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_settings.setOnClickListener {
                    findNavController().navigate(R.id.action_noteCollectionFragment_to_settingFragment)
                    bottomSheetDialog.dismiss()
                }
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortBotomm() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetSortView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_bottom_sheet_sort_note, requireView().findViewById(R.id.bottom_sheet_sort_container))

        if (mNoteViewModel.getIsRecent()) {
            bottomSheetSortView.sort_recent_img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            bottomSheetSortView.sort_recent_text.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
        else {
            bottomSheetSortView.sort_front_img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            bottomSheetSortView.sort_front_text.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }

        bottomSheetSortView.bottom_sort_recent.setOnClickListener {
            bottomSheetDialog.dismiss()
            mNoteViewModel.rearrangeNote(true, true)
        }
        bottomSheetSortView.bottom_sort_front.setOnClickListener {
            bottomSheetDialog.dismiss()
            mNoteViewModel.rearrangeNote(false, true)
        }
        bottomSheetDialog.setContentView(bottomSheetSortView)
        bottomSheetDialog.show()
    }

    private fun flipToFront() {
        mNoteViewModel.updateNoteFront(currentId, true)
    }

    private fun flipToBack() {
        mNoteViewModel.updateNoteFront(currentId, false)
    }

    private fun deleteAllNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            /// ALL???????????????????????????????????????????
            mNoteViewModel.deleteAllNotes(currentId)
            mNoteViewModel.resetNoteNum(currentId)
            Toast.makeText(requireContext(), getString(R.string.all_notes_are_removed), Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.delete_all_notes_query))
        builder.setMessage(getString(R.string.all_notes_will_be_completely_deleted))
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    }
}

