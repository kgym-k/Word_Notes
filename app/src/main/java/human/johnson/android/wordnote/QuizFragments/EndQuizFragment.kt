package human.johnson.android.wordnote.QuizFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.postDelayed
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import human.johnson.android.wordnote.MyApplication.Companion.currentId
import human.johnson.android.wordnote.R
import kotlinx.android.synthetic.main.fragment_end_quiz.*
import kotlinx.android.synthetic.main.fragment_end_quiz.view.*

class EndQuizFragment : Fragment() {
    private val args by navArgs<EndQuizFragmentArgs>()
    lateinit var mHandler: Handler
    lateinit var runnable: Runnable
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index_end")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_end_quiz, container, false)

        // bar
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val progressText = view.findViewById<TextView>(R.id.progress_text)
        val rate = 100 * args.correctNum / args.quizSIze

        mHandler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if (index <= rate) {
                progressText.text = "$index%"
                progressBar.progress = index
                index++
                mHandler.postDelayed(runnable, 20)
            }
            else {
                progressText.text = "$rate%"
                mHandler.removeCallbacks(runnable)
            }
        }
        mHandler.post(runnable)

        // comment
        view.quiz_comment_text.text = when (rate) {
            in 90..100 -> getString(R.string.excellent)
            in 70..89 -> getString(R.string.good)
            in 50..69 -> getString(R.string.fair)
            else -> ""
        }

        view.finish_btn_quiz.setOnClickListener {
            val action =
                EndQuizFragmentDirections.actionEndQuizFragmentToNoteCollectionFragment(currentId)
            findNavController().navigate(action)
        }
        view.try_again_btn_quiz.setOnClickListener {
            findNavController().navigate(R.id.action_endQuizFragment_to_startQuizFragment)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("index_end", index)
    }
}