package human.johnson.android.wordnote.QuizFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import java.util.*
import kotlin.collections.ArrayList

class QuizFragment : Fragment() {
    private val args by navArgs<QuizFragmentArgs>()
    private lateinit var mNoteViewModel: NoteViewModel

    private var note_size = 0
    private var quiz_size = 0
    private var quiz_list: List<Int> = emptyList()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        note_size = args.noteSize
        quiz_size =
            if (note_size < args.cardNum) { note_size }
            else { args.cardNum }
        if (savedInstanceState != null) {
            quiz_list = savedInstanceState.getIntegerArrayList("quizList")!!.toList()
            index = savedInstanceState.getInt("index")
        } else {
            quiz_list =
                if (args.cardOrder == 0) { createRand(note_size, quiz_size) }
                else { (0..quiz_size - 1).toMutableList() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_quiz, container, false)

        // flip
        view.front_card_quiz.setOnClickListener {
            view.front_card_quiz.visibility = View.INVISIBLE
            view.back_card_quiz.visibility = View.VISIBLE
        }
        view.back_card_quiz.setOnClickListener {
            view.back_card_quiz.visibility = View.INVISIBLE
            view.front_card_quiz.visibility = View.VISIBLE
        }

        // view model
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        when (args.cardFrom) {
            0 -> {
                mNoteViewModel.setStar(false)
                mNoteViewModel.setCheck(false)
                mNoteViewModel.setNotStar(false)
                mNoteViewModel.setNotCheck(false)
            }
            1 -> {
                mNoteViewModel.setStar(true)
                mNoteViewModel.setCheck(false)
                mNoteViewModel.setNotStar(false)
                mNoteViewModel.setNotCheck(false)
            }
            2 -> {
                mNoteViewModel.setStar(false)
                mNoteViewModel.setCheck(true)
                mNoteViewModel.setNotStar(false)
                mNoteViewModel.setNotCheck(false)
            }
            3 -> {
                mNoteViewModel.setStar(false)
                mNoteViewModel.setCheck(false)
                mNoteViewModel.setNotStar(true)
                mNoteViewModel.setNotCheck(false)
            }
            4 -> {
                mNoteViewModel.setStar(false)
                mNoteViewModel.setCheck(false)
                mNoteViewModel.setNotStar(false)
                mNoteViewModel.setNotCheck(true)
            }
        }
        if (args.cardOrder == 2) {
            mNoteViewModel.setRecent(false)
        }
        else {
            mNoteViewModel.setRecent(true)
        }

        var init = true
        var correct_num = 0
        mNoteViewModel.note.observe(viewLifecycleOwner, Observer { note ->
            if (init) {
                updateQuiz(note, note_size, quiz_list[index])

                // onClick good
                view.good_btn_quiz.setOnClickListener {
                    mNoteViewModel.updateNoteCheck(note[index].id, false)
                    index += 1
                    correct_num += 1
                    if (index < note_size) {
                        view.back_card_quiz.visibility = View.INVISIBLE
                        view.front_card_quiz.visibility = View.VISIBLE
                        updateQuiz(note, note_size, quiz_list[index])
                    }
                    else {
                        val action = QuizFragmentDirections.actionQuizFragmentToEndQuizFragment(quiz_size, correct_num)
                        findNavController().navigate(action)
                    }
                }

                // onClick bad
                view.bad_btn_quiz.setOnClickListener {
                    mNoteViewModel.updateNoteCheck(note[index].id, true)
                    index += 1
                    if (index < note_size) {
                        view.back_card_quiz.visibility = View.INVISIBLE
                        view.front_card_quiz.visibility = View.VISIBLE
                        updateQuiz(note, note_size, quiz_list[index])
                    }
                    else {
                        val action = QuizFragmentDirections.actionQuizFragmentToEndQuizFragment(quiz_size, correct_num)
                        findNavController().navigate(action)
                    }
                }

                init = false
            }
        })

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putIntegerArrayList("quizList", ArrayList(quiz_list))
        outState.putInt("index", index)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            findNavController().navigateUp()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(getString(R.string.quit_quiz_query))
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    }

    private fun createRand(note_size: Int, quiz_size: Int): MutableList<Int> {
        var note_size = note_size
        var note_list = (0 until note_size).toMutableList()
        val quiz_list = mutableListOf<Int>()
        var rand = 0
        for (i in 0 until quiz_size) {
            rand = Random().nextInt(note_size)
            quiz_list.add(note_list[rand])
            note_list.removeAt(rand)
            note_size -= 1
        }
        return quiz_list
    }

    private fun updateQuiz(note: List<Note>, note_size: Int, i: Int) {
        requireView().front_check_quiz.visibility =
            if (note[i].checked) View.VISIBLE
            else View.GONE
        requireView().front_num_quiz.text = (index + 1).toString() + "/" + note_size.toString()
        requireView().front_word_txt_quiz.text = note[i].word
        requireView().front_meaning_txt_quiz.text = note[i].meaning
        requireView().front_memo_txt_quiz.text = note[i].memo

        requireView().back_check_quiz.visibility =
            if (note[i].checked) View.VISIBLE
            else View.GONE
        requireView().back_num_quiz.text = (index + 1).toString() + "/" + note_size.toString()
        requireView().back_word_txt_quiz.text = note[i].word
        requireView().back_meaning_txt_quiz.text = note[i].meaning
        requireView().back_memo_txt_quiz.text = note[i].memo

        // color
        when (note[i].color) {
            1 ->  {
                requireView().front_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect1
                ))
                requireView().back_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect1
                ))
            }
            2 ->  {
                requireView().front_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect2
                ))
                requireView().back_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect2
                ))
            }
            3 ->  {
                requireView().front_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect3
                ))
                requireView().back_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect3
                ))
            }
            4 ->  {
                requireView().front_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect4
                ))
                requireView().back_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect4
                ))
            }
            5 ->  {
                requireView().front_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect5
                ))
                requireView().back_line_quiz.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorSelect5
                ))
            }
        }

        // star
        if (note[i].star) {
            requireView().front_star_emp_quiz.visibility = View.INVISIBLE
            requireView().back_star_emp_quiz.visibility = View.INVISIBLE
            requireView().front_star_full_quiz.visibility = View.VISIBLE
            requireView().back_star_full_quiz.visibility = View.VISIBLE
        }
        else {
            requireView().front_star_full_quiz.visibility = View.INVISIBLE
            requireView().back_star_full_quiz.visibility = View.INVISIBLE
            requireView().front_star_emp_quiz.visibility = View.VISIBLE
            requireView().back_star_emp_quiz.visibility = View.VISIBLE
        }
    }
}
