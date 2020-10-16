package human.johnson.android.wordnote.NoteFragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import human.johnson.android.wordnote.MyApplication.Companion.currentId
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note_edit.*
import kotlinx.android.synthetic.main.fragment_note_edit.view.*
import human.johnson.android.wordnote.MyApplication.Companion.star_flag

class NoteEditFragment : Fragment() {

    private val args by navArgs<NoteEditFragmentArgs>()
    var color: Int = 1

    private lateinit var mNoteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_edit, container, false)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // star
        if (star_flag) {
            view.note_edit_star_emp.visibility = View.INVISIBLE
            view.note_edit_star_full.visibility = View.VISIBLE
        }
        else {
            view.note_edit_star_full.visibility = View.INVISIBLE
            view.note_edit_star_emp.visibility = View.VISIBLE
        }

        view.note_edit_star_emp.setOnClickListener {
            view.note_edit_star_emp.visibility = View.INVISIBLE
            view.note_edit_star_full.visibility = View.VISIBLE
        }
        view.note_edit_star_full.setOnClickListener {
            view.note_edit_star_full.visibility = View.INVISIBLE
            view.note_edit_star_emp.visibility = View.VISIBLE
        }

        // color
        view.colorSelect1_selected.visibility = View.VISIBLE

        fun unselectColor() {
            when (color) {
                1 -> view.colorSelect1_selected.visibility = View.INVISIBLE
                2 -> view.colorSelect2_selected.visibility = View.INVISIBLE
                3 -> view.colorSelect3_selected.visibility = View.INVISIBLE
                4 -> view.colorSelect4_selected.visibility = View.INVISIBLE
                5 -> view.colorSelect5_selected.visibility = View.INVISIBLE
            }
        }

        // color1
        view.colorSelect1_unselected.setOnClickListener{
            unselectColor()
            view.colorSelect1_selected.visibility = View.VISIBLE
            color = 1
        }

        // color2
        view.colorSelect2_unselected.setOnClickListener{
            unselectColor()
            view.colorSelect2_selected.visibility = View.VISIBLE
            color = 2
        }

        // color3
        view.colorSelect3_unselected.setOnClickListener{
            unselectColor()
            view.colorSelect3_selected.visibility = View.VISIBLE
            color = 3
        }

        // color4
        view.colorSelect4_unselected.setOnClickListener{
            unselectColor()
            view.colorSelect4_selected.visibility = View.VISIBLE
            color = 4
        }

        // color5
        view.colorSelect5_unselected.setOnClickListener {
            unselectColor()
            view.colorSelect5_selected.visibility = View.VISIBLE
            color = 5
        }

        view.create_note.setOnClickListener {
            insertDataToDatabase(view)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_content -> { insertDataToDatabase(requireView()) }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase(view: View) {
        val word = note_editText_word.text.toString()
        val meaning = note_editText_meaning.text.toString()
        val memo = note_editText_memo.text.toString()
        val front = true
        val star = view.note_edit_star_full.isVisible
        val check = false

        if(inputCheck(word, meaning)) {
            val note = Note(0, args.currentId, word, meaning, memo, front, star, check, color)
            mNoteViewModel.addNote(note)
            mNoteViewModel.updateNoteNum(currentId, 1)

            Toast.makeText(requireContext(), getString(R.string.added), Toast.LENGTH_LONG).show()
            val action = NoteEditFragmentDirections
                .actionNoteEditFragmentToNoteCollectionFragment(args.currentId)
            view.findNavController().navigate(action)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.please_fill_out_front_and_back), Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(word: String, meaning: String): Boolean {
        return !(TextUtils.isEmpty(word)) && !(TextUtils.isEmpty(meaning))
    }

    override fun onStop() {
        super.onStop()

        // close keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = getView()
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}