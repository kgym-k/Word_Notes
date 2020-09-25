package human.johnson.android.wordnote.ShelfFragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Shelf
import human.johnson.android.wordnote.viewmodel.ShelfViewModel
import kotlinx.android.synthetic.main.fragment_note_update.view.*
import kotlinx.android.synthetic.main.fragment_shelf_update.*
import kotlinx.android.synthetic.main.fragment_shelf_update.view.*

class ShelfUpdateFragment : Fragment() {

    private val args by navArgs<ShelfUpdateFragmentArgs>()
    var color: Int = 0

    private lateinit var mShelfViewModel: ShelfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shelf_update, container, false)

        mShelfViewModel = ViewModelProvider(this).get(ShelfViewModel::class.java)

        view.shelf_updateText_text.setText(args.currentShelf.subject)

        // color
        color = args.currentShelf.color
        when (color) {
            1 -> view.colorSelect1_selected_shelf_update.visibility = View.VISIBLE
            2 -> view.colorSelect2_selected_shelf_update.visibility = View.VISIBLE
            3 -> view.colorSelect3_selected_shelf_update.visibility = View.VISIBLE
            4 -> view.colorSelect4_selected_shelf_update.visibility = View.VISIBLE
            5 -> view.colorSelect5_selected_shelf_update.visibility = View.VISIBLE
        }

        fun unselectColor() {
            when (color) {
                1 -> view.colorSelect1_selected_shelf_update.visibility = View.INVISIBLE
                2 -> view.colorSelect2_selected_shelf_update.visibility = View.INVISIBLE
                3 -> view.colorSelect3_selected_shelf_update.visibility = View.INVISIBLE
                4 -> view.colorSelect4_selected_shelf_update.visibility = View.INVISIBLE
                5 -> view.colorSelect5_selected_shelf_update.visibility = View.INVISIBLE
            }
        }

        // color1
        view.colorSelect1_unselected_shelf_update.setOnClickListener{
            unselectColor()
            view.colorSelect1_selected_shelf_update.visibility = View.VISIBLE
            color = 1
        }

        // color2
        view.colorSelect2_unselected_shelf_update.setOnClickListener{
            unselectColor()
            view.colorSelect2_selected_shelf_update.visibility = View.VISIBLE
            color = 2
        }

        // color3
        view.colorSelect3_unselected_shelf_update.setOnClickListener{
            unselectColor()
            view.colorSelect3_selected_shelf_update.visibility = View.VISIBLE
            color = 3
        }

        // color4
        view.colorSelect4_unselected_shelf_update.setOnClickListener{
            unselectColor()
            view.colorSelect4_selected_shelf_update.visibility = View.VISIBLE
            color = 4
        }

        // color5
        view.colorSelect5_unselected_shelf_update.setOnClickListener {
            unselectColor()
            view.colorSelect5_selected_shelf_update.visibility = View.VISIBLE
            color = 5
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun updateItem() {
        val subject = shelf_updateText_text.text.toString()

        if (inputCheck(subject)) {
            val updatedShelf = Shelf(args.currentShelf.id, subject, color, args.currentShelf.noteNum)
            mShelfViewModel.updateShelf(updatedShelf)
            Toast.makeText(requireContext(), getString(R.string.folder_is_updated), Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_shelfUpdateFragment_to_shelfFragment)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.please_fill_out_name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(subject: String): Boolean {
        return !(TextUtils.isEmpty(subject))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_update_content) {
            updateItem()
        }
        if(item.itemId == R.id.menu_update_delete) {
            deleteShelf()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteShelf() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mShelfViewModel.deleteShelf(args.currentShelf)
            mShelfViewModel.deleteAllNotes(args.currentShelf.id)
            Toast.makeText(requireContext(), getString(R.string.this_folder_is_removed), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_shelfUpdateFragment_to_shelfFragment)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->}
        builder.setTitle(getString(R.string.delete_this_folder_query))
        builder.setMessage(getString(R.string.this_folder_will_be_completely_deleted))
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    }

    override fun onStop() {
        super.onStop()

        // close keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = getView()
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}