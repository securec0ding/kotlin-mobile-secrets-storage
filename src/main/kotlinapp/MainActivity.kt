//
//  MainActivity.kt
//  Veracoin
//
//  This is a mocked Activty where most of the lesson code changes
//  will be made.
//

package kotlinapp

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import firebase.Firebase
import firebase.FirebaseAnalytics

lateinit var firebaseAnalytics: FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start 3rd Party API
        ThirdPartyAPI.start("BadApiCoAccount")

        firebaseAnalytics = Firebase.analytics

        // Login user:
        // This automatic login is for demonstration purposes only. 
        // In a real application, users should always securely log in.
        //UserManager.loginUser(101)

        handleIntent(getIntent())
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }  

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.getData()

        if (Intent.ACTION_VIEW == appLinkAction) {
            // Partial validation of the URL with lastPathSegment is not secure. 
            // This should be updated to use an allowlist for full URL validation. 
            appLinkData?.lastPathSegment?.also { route ->
                if (route == "showfriend") {
                    val userId = appLinkData.getQueryParameter("id")
                    userId?.also { u -> 
                        Router.showFriend(u.toInt())
                    }
                }
            }
        }
    }

}