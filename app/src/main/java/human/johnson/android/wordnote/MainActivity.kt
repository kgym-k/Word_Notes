package human.johnson.android.wordnote

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.ads.consent.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private var mAdView: AdView? = null
    private lateinit var form: ConsentForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Euro
        // getConsentInfo()

        // ads initialize
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_id))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)

        // nav
        setupActionBarWithNavController(findNavController(R.id.fragment))

        // initial settings ?
        val preferences = getPreferences(Context.MODE_PRIVATE)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}