package human.johnson.android.wordnote.ShelfFragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Shelf
import human.johnson.android.wordnote.viewmodel.ShelfViewModel
import kotlinx.android.synthetic.main.fragment_note_edit.view.*
import kotlinx.android.synthetic.main.fragment_shelf_edit.*
import kotlinx.android.synthetic.main.fragment_shelf_edit.view.*

class ShelfEditFragment : Fragment() {

    private lateinit var mShelfViewModel: ShelfViewModel
    var color: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shelf_edit, container, false)

        mShelfViewModel = ViewModelProvider(this).get(ShelfViewModel::class.java)

        // color
        view.colorSelect1_selected_shelf_edit.visibility = View.VISIBLE

        fun unselectColor() {
            when (color) {
                1 -> view.colorSelect1_selected_shelf_edit.visibility = View.INVISIBLE
                2 -> view.colorSelect2_selected_shelf_edit.visibility = View.INVISIBLE
                3 -> view.colorSelect3_selected_shelf_edit.visibility = View.INVISIBLE
                4 -> view.colorSelect4_selected_shelf_edit.visibility = View.INVISIBLE
                5 -> view.colorSelect5_selected_shelf_edit.visibility = View.INVISIBLE
            }
        }

        // color1
        view.colorSelect1_unselected_shelf_edit.setOnClickListener{
            unselectColor()
            view.colorSelect1_selected_shelf_edit.visibility = View.VISIBLE
            color = 1
        }

        // color2
        view.colorSelect2_unselected_shelf_edit.setOnClickListener{
            unselectColor()
            view.colorSelect2_selected_shelf_edit.visibility = View.VISIBLE
            color = 2
        }

        // color3
        view.colorSelect3_unselected_shelf_edit.setOnClickListener{
            unselectColor()
            view.colorSelect3_selected_shelf_edit.visibility = View.VISIBLE
            color = 3
        }

        // color4
        view.colorSelect4_unselected_shelf_edit.setOnClickListener{
            unselectColor()
            view.colorSelect4_selected_shelf_edit.visibility = View.VISIBLE
            color = 4
        }

        // color5
        view.colorSelect5_unselected_shelf_edit.setOnClickListener {
            unselectColor()
            view.colorSelect5_selected_shelf_edit.visibility = View.VISIBLE
            color = 5
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_content -> { insertDataToDatabase() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val subject = shelf_editText_text.text.toString()

        if(inputCheck(subject)) {
            val shelf = Shelf(0, subject, color)
            mShelfViewModel.addShelf(shelf)

            Toast.makeText(requireContext(), getString(R.string.added), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_shelfEditFragment_to_shelfFragment)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.please_fill_out_name), Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(subject: String): Boolean {
        return !(TextUtils.isEmpty(subject))
    }

    override fun onStop() {
        super.onStop()

        // close keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = getView()
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}