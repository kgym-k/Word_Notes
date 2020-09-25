package human.johnson.android.wordnote.ShelfFragments

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.viewmodel.ShelfViewModel
import kotlinx.android.synthetic.main.fragment_shelf.*
import kotlinx.android.synthetic.main.fragment_shelf.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_note.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_shelf.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_sort_note.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_sort_note.view.bottom_sort_front
import kotlinx.android.synthetic.main.layout_bottom_sheet_sort_shelf.view.*
import kotlinx.android.synthetic.main.shelf_row.view.*

class ShelfFragment : Fragment() {

    private lateinit var mShelfViewModel: ShelfViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShelfAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shelf, container, false)

        mShelfViewModel = ViewModelProvider(this).get(ShelfViewModel::class.java)
        recyclerView = view.recyclerview_shelf
        adapter = ShelfAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mShelfViewModel.shelf.observe(viewLifecycleOwner, Observer { shelf ->
            adapter.setData(shelf)

            // visibility
            val shelf_imageView: ImageView = view.findViewById(R.id.shelf_imageView)
            val shelf_text: TextView= view.findViewById(R.id.shelf_textView)
            if  (adapter.itemCount == 0) {
                shelf_imageView.visibility = View.VISIBLE
                shelf_text.visibility = View.VISIBLE
            }
            else {
                shelf_imageView.visibility = View.INVISIBLE
                shelf_text.visibility = View.INVISIBLE
            }
        })

        // fab
        view.fab_shelf.setOnClickListener {
            findNavController().navigate(R.id.action_shelfFragment_to_shelfEditFragment)
        }

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
                fab_shelf.visibility = View.GONE
                menu.getItem(1).setVisible(false)
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                fab_shelf.visibility = View.VISIBLE
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
                mShelfViewModel.setStr(newText)
                mShelfViewModel.rearrangeNote(mShelfViewModel.getIsRecent())
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
                    .inflate(R.layout.layout_bottom_sheet_shelf, requireView()
                        .findViewById(R.id.bottom_sheet_container_shelf))
                bottomSheetView.bottom_sort_shelf.setOnClickListener {
                    sortBotomm()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_delete_shelf.setOnClickListener {
                    deleteAllShelf()
                    bottomSheetDialog.dismiss()
                }
                bottomSheetView.bottom_settings_shelf.setOnClickListener {
                    findNavController().navigate(R.id.action_shelfFragment_to_settingFragment)
                    bottomSheetDialog.dismiss()
                }
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortBotomm() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetSortView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_bottom_sheet_sort_shelf, requireView().findViewById(R.id.bottom_sheet_sort_container_shelf))

        if (mShelfViewModel.getIsRecent()) {
            bottomSheetSortView.sort_recent_check_shelf.visibility = View.VISIBLE
        }
        else {
            bottomSheetSortView.sort_name_check_shelf.visibility = View.VISIBLE
        }

        bottomSheetSortView.bottom_sort_recent_shelf.setOnClickListener {
            bottomSheetDialog.dismiss()
            mShelfViewModel.rearrangeNote(true)
        }
        bottomSheetSortView.bottom_sort_name_shelf.setOnClickListener {
            bottomSheetDialog.dismiss()
            mShelfViewModel.rearrangeNote(false)
        }
        bottomSheetDialog.setContentView(bottomSheetSortView)
        bottomSheetDialog.show()
    }

    private fun deleteAllShelf() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mShelfViewModel.deleteAllShelves()
            mShelfViewModel.deleteAllNotesInDb()
            Toast.makeText(requireContext(), getString(R.string.all_folders_are_removed), Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->}
        builder.setTitle(getString(R.string.delete_all_folders_query))
        builder.setMessage(getString(R.string.all_folders_will_be_completely_deleted))
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    }
}