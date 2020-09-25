package human.johnson.android.wordnote.NoteFragments

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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import human.johnson.android.wordnote.MyApplication
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note_update.*
import kotlinx.android.synthetic.main.fragment_note_update.view.*
import human.johnson.android.wordnote.MyApplication.Companion.star_flag

class NoteUpdateFragment : Fragment() {

    private val args by navArgs<NoteUpdateFragmentArgs>()
    var color: Int = 0

    private lateinit var mNoteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_update, container, false)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        view.note_updateText_word.setText(args.currentNote.word)
        view.note_updateText_meaning.setText(args.currentNote.meaning)
        view.note_updateText_memo.setText(args.currentNote.memo)
        if (args.currentNote.star) {
            view.note_update_star_emp.visibility = View.INVISIBLE
            view.note_update_star_full.visibility = View.VISIBLE
        }
        else {
            view.note_update_star_full.visibility = View.INVISIBLE
            view.note_update_star_emp.visibility = View.VISIBLE
        }

        // star
        view.note_update_star_emp.setOnClickListener {
            view.note_update_star_emp.visibility = View.INVISIBLE
            view.note_update_star_full.visibility = View.VISIBLE
        }
        view.note_update_star_full.setOnClickListener {
            view.note_update_star_full.visibility = View.INVISIBLE
            view.note_update_star_emp.visibility = View.VISIBLE
        }

        // color
        color = args.currentNote.color
        when (color) {
            1 -> view.colorSelect1_selected_update.visibility = View.VISIBLE
            2 -> view.colorSelect2_selected_update.visibility = View.VISIBLE
            3 -> view.colorSelect3_selected_update.visibility = View.VISIBLE
            4 -> view.colorSelect4_selected_update.visibility = View.VISIBLE
            5 -> view.colorSelect5_selected_update.visibility = View.VISIBLE
        }

        fun unselectColor() {
            when (color) {
                1 -> view.colorSelect1_selected_update.visibility = View.INVISIBLE
                2 -> view.colorSelect2_selected_update.visibility = View.INVISIBLE
                3 -> view.colorSelect3_selected_update.visibility = View.INVISIBLE
                4 -> view.colorSelect4_selected_update.visibility = View.INVISIBLE
                5 -> view.colorSelect5_selected_update.visibility = View.INVISIBLE
            }
        }

        // color1
        view.colorSelect1_unselected_update.setOnClickListener{
            unselectColor()
            view.colorSelect1_selected_update.visibility = View.VISIBLE
            color = 1
        }

        // color2
        view.colorSelect2_unselected_update.setOnClickListener{
            unselectColor()
            view.colorSelect2_selected_update.visibility = View.VISIBLE
            color = 2
        }

        // color3
        view.colorSelect3_unselected_update.setOnClickListener{
            unselectColor()
            view.colorSelect3_selected_update.visibility = View.VISIBLE
            color = 3
        }

        // color4
        view.colorSelect4_unselected_update.setOnClickListener{
            unselectColor()
            view.colorSelect4_selected_update.visibility = View.VISIBLE
            color = 4
        }

        // color5
        view.colorSelect5_unselected_update.setOnClickListener {
            unselectColor()
            view.colorSelect5_selected_update.visibility = View.VISIBLE
            color = 5
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_update_content) {
            updateItem(requireView())
        }
        if(item.itemId == R.id.menu_update_delete) {
            deleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem(view: View) {
        val word = note_updateText_word.text.toString()
        val meaning = note_updateText_meaning.text.toString()
        val memo = note_updateText_memo.text.toString()
        val front = args.currentNote.front
        val star = note_update_star_full.isVisible

        if (inputCheck(word, meaning)) {
            val updatedNote = Note(args.currentNote.id, args.currentNote.shelfId, word, meaning, memo, front, star, color)
            mNoteViewModel.updateNote(updatedNote)
            Toast.makeText(requireContext(), getString(R.string.card_is_updated), Toast.LENGTH_SHORT).show()

            val action = NoteUpdateFragmentDirections
                .actionNoteUpdateFragmentToNoteCollectionFragment(args.currentNote.shelfId)
            view.findNavController().navigate(action)
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.please_fill_out_front_and_back), Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(word: String, meaning: String): Boolean {
        return !(TextUtils.isEmpty(word)) && !(TextUtils.isEmpty(meaning))
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mNoteViewModel.deleteNote(args.currentNote)
            mNoteViewModel.updateNoteNum(args.currentNote.shelfId, -1)
            Toast.makeText(requireContext(), getString(R.string.this_card_is_removed), Toast.LENGTH_SHORT).show()

            val action = NoteUpdateFragmentDirections.actionNoteUpdateFragmentToNoteCollectionFragment(args.currentNote.shelfId)
            requireView().findNavController().navigate(action)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->}
        builder.setTitle(getString(R.string.delete_this_card_query))
        builder.setMessage(getString(R.string.this_card_will_be_completely_deleted))
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
