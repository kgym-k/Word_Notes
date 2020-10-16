package human.johnson.android.wordnote

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.ads.consent.ConsentForm
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import human.johnson.android.wordnote.data.MyDatabase
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mAdView: AdView? = null
    private lateinit var form: ConsentForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ads initialize
        MobileAds.initialize(applicationContext, getString(R.string.app_id))
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)

        // Euro
        // getConsentInfo()

        // nav
        setupActionBarWithNavController(findNavController(R.id.fragment))

        // initial settings ?
        val preferences = getPreferences(Context.MODE_PRIVATE)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.shelfFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        return when(navController.currentDestination?.id) {
            R.id.quizFragment -> {
                showAlertDialog()
                true
            }
            else -> navController.navigateUp() || super.onSupportNavigateUp()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            findNavController(R.id.fragment).navigateUp()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(getString(R.string.quit_quiz_query))
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    /*
    private fun getConsentInfo() {
        val consentInformation = ConsentInformation.getInstance(this)
        val publisherIds = arrayOf(" ")
        consentInformation.requestConsentInfoUpdate(
            publisherIds,
            object : ConsentInfoUpdateListener {
                override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                    // User's consent status successfully updated.

                    val isEuropeanUser = ConsentInformation
                        .getInstance(this@MainActivity).isRequestLocationInEeaOrUnknown()

                    if (isEuropeanUser) {
                        when (consentStatus) {
                            ConsentStatus.PERSONALIZED -> personalizedAds(true)
                            ConsentStatus.NON_PERSONALIZED -> personalizedAds(false)
                            ConsentStatus.UNKNOWN -> getConsentForm()
                        }

                    }
                }

                override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                    // User's consent status failed to update.
                }
            })
    }

    private fun personalizedAds(isPersonalized: Boolean) {
        if (isPersonalized) {
            mAdView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView?.loadAd(adRequest)
        }
        else {
            val extras = Bundle()
            extras.putString("npa", "1")

            val adRequest =
                AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                    .build()
            mAdView?.loadAd(adRequest)
        }
    }

    private fun getConsentForm() {
        var privacyUrl: URL? = null
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = URL("https://")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }
        form = ConsentForm.Builder(this, privacyUrl)
            .withListener(object : ConsentFormListener() {
                override fun onConsentFormLoaded() {
                    // Consent form loaded successfully.
                    form.show()
                }

                override fun onConsentFormOpened() {
                    // Consent form was displayed.
                }

                override fun onConsentFormClosed(
                    consentStatus: ConsentStatus, userPrefersAdFree: Boolean
                ) {
                    // Consent form was closed.
                    if (consentStatus.equals(ConsentStatus.PERSONALIZED)) {
                        personalizedAds(true)
                    }
                    else {
                        personalizedAds(false)
                    }
                }

                override fun onConsentFormError(errorDescription: String) {
                    // Consent form error.
                }
            })
            .withPersonalizedAdsOption()
            .withNonPersonalizedAdsOption()
            .build()
        form.load()
    }
    */
}