package human.johnson.android.wordnote.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import human.johnson.android.wordnote.R
import kotlinx.android.synthetic.main.fragment_end_quiz.view.*
import kotlinx.android.synthetic.main.fragment_third_screen.view.*

class ThirdScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third_screen, container, false)

        return view
    }
}