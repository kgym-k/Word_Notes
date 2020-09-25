package human.johnson.android.wordnote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_setting, container, false)
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()

        view.terms_and_conditions.setOnClickListener {
            val uri = Uri.parse("https://sites.google.com/view/wordnotes/terms-and-conditions?authuser=1")
            customTabsIntent.launchUrl(requireContext(), uri)
        }
        view.privacy_policy.setOnClickListener {
            val uri = Uri.parse("https://sites.google.com/view/wordnotes/privacy-policy?authuser=1")
            customTabsIntent.launchUrl(requireContext(), uri)
        }

        return view
    }
}