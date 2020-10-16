package human.johnson.android.wordnote.QuizFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.fragment_start_quiz.*
import kotlinx.android.synthetic.main.fragment_start_quiz.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_note.view.*
import kotlinx.android.synthetic.main.quiz_instruction.*
import kotlinx.android.synthetic.main.quiz_instruction.view.*

class StartQuizFragment : Fragment() {
    private lateinit var mNoteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_quiz, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onStart() {
        super.onStart()

        // set array adapters
        (requireView().textField_select_cards_from.editText as? AutoCompleteTextView)?.setText(getString(R.string.all_quiz))
        val items_select_from =
            listOf(getString(R.string.all_quiz), getString(R.string.stared_quiz), getString(R.string.checked_quiz), getString(R.string.not_stared), getString(R.string.not_checked))
        val adapter_select_from = ArrayAdapter(requireContext(), R.layout.list_item, items_select_from)
        (requireView().textField_select_cards_from.editText as? AutoCompleteTextView)?.setAdapter(adapter_select_from)

        (requireView().textField_card_num.editText as? AutoCompleteTextView)?.setText("10")
        val items_card_num =
            listOf("10", "20", "50", "100")
        val adapter_card_num = ArrayAdapter(requireContext(), R.layout.list_item, items_card_num)
        (requireView().textField_card_num.editText as? AutoCompleteTextView)?.setAdapter(adapter_card_num)

        (requireView().textField_card_order.editText as? AutoCompleteTextView)?.setText(getString(R.string.shuffle))
        val items_card_order =
            listOf(getString(R.string.shuffle), getString(R.string.recently_added), getString(R.string.front_card))
        val adapter_card_order = ArrayAdapter(requireContext(), R.layout.list_item, items_card_order)
        (requireView().textField_card_order.editText as? AutoCompleteTextView)?.setAdapter(adapter_card_order)

        // get note size
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        mNoteViewModel.setStar(false)
        mNoteViewModel.setCheck(false)
        mNoteViewModel.setNotStar(false)
        mNoteViewModel.setNotCheck(false)
        var note_size = 0
        mNoteViewModel.note.observe(viewLifecycleOwner, Observer { note ->
            note_size = note.size
        })

        // click listener
        requireView().start_quiz.setOnClickListener {
            var card_from = -1
            when ((requireView().textField_select_cards_from.editText as? AutoCompleteTextView)?.text.toString()) {
                "All" -> card_from = 0
                "Stared" -> card_from = 1
                "Checked" -> card_from = 2
                "Not Stared" -> card_from = 3
                "Not Checked" -> card_from = 4
                "全て" -> card_from = 0
                "スター" -> card_from = 1
                "チェック" -> card_from = 2
                "スター以外" -> card_from = 3
                "チェック以外" -> card_from = 4
            }

            val card_num = (requireView().textField_card_num.editText as? AutoCompleteTextView)?.text.toString().toInt()

            var card_order = -1
            when ((requireView().textField_card_order.editText as? AutoCompleteTextView)?.text.toString()) {
                "Shuffle" -> card_order = 0
                "Recently Added" -> card_order = 1
                "Front Card" -> card_order = 2
                "シャッフル" -> card_order = 0
                "最近追加した項目" -> card_order = 1
                "オモテ" -> card_order = 2
            }

            when (card_from) {
                0 -> {
                    mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), _is_star = false, _is_check = false, _not_star = false, _not_check = false)
                    if (note_size < 1) {
                        Toast.makeText(requireContext(), getString(R.string.there_is_no_card), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val action = StartQuizFragmentDirections.actionStartQuizFragmentToQuizFragment(card_from, card_num, card_order, note_size)
                        findNavController().navigate(action)
                    }
                }
                1 -> {
                    mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), _is_star = true, _is_check = false, _not_star = false, _not_check = false)
                    if (note_size < 1) {
                        Toast.makeText(requireContext(), getString(R.string.there_is_no_card), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val action = StartQuizFragmentDirections.actionStartQuizFragmentToQuizFragment(card_from, card_num, card_order, note_size)
                        findNavController().navigate(action)
                    }
                }
                2 -> {
                    mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), _is_star = false, _is_check = true, _not_star = false, _not_check = false)
                    if (note_size < 1) {
                        Toast.makeText(requireContext(), getString(R.string.there_is_no_card), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val action = StartQuizFragmentDirections.actionStartQuizFragmentToQuizFragment(card_from, card_num, card_order, note_size)
                        findNavController().navigate(action)
                    }
                }
                3 -> {
                    mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), _is_star = false, _is_check = false, _not_star = true, _not_check = false)
                    if (note_size < 1) {
                        Toast.makeText(requireContext(), getString(R.string.there_is_no_card), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val action = StartQuizFragmentDirections.actionStartQuizFragmentToQuizFragment(card_from, card_num, card_order, note_size)
                        findNavController().navigate(action)
                    }
                }
                4 -> {
                    mNoteViewModel.rearrangeNote(mNoteViewModel.getIsRecent(), _is_star = false, _is_check = false, _not_star = false, _not_check = true)
                    if (note_size < 1) {
                        Toast.makeText(requireContext(), getString(R.string.there_is_no_card), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val action = StartQuizFragmentDirections.actionStartQuizFragmentToQuizFragment(card_from, card_num, card_order, note_size)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quiz_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_quiz_info) {
            showQuizInfo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showQuizInfo() {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.quiz_instruction, null)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        view.findViewById<ImageButton>(R.id.quiz_instruction_close).setOnClickListener {
            dialog.dismiss()
        }
    }
}