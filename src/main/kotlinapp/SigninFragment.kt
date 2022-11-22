//
//  SigninFragment.kt
//  Veracoin
//

package kotlinapp

import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log

import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.Fragment
import androidx.browser.customtabs.CustomTabsIntent

import firebase.FirebaseAnalytics

class SigninFragment : BaseLoggingFragment() {
    companion object {
        private val TAG: String? = SigninFragment::class.simpleName
    }

    override fun onCreateView(
            inflater: LayoutInflater, 
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.edittext_email)
        val passwordEditText = view.findViewById<EditText>(R.id.edittext_password)

        view.findViewById<Button>(R.id.button_signin).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val email = emailEditText.getText().toString()
                val password = passwordEditText.getText().toString()

                Log.d(TAG, "Signin email: $email password: $password")

                firebaseAnalytics.logEvent("Sigin") {
                    param("email", email)
                    param("password", password)
                }

                UserManager.login(email, password) {
                    val res = it.result
                    if (res) {
                        Router.showHome()
                    } else {
                        toast("Email or password incorrect")
                    }
                }

            }
        })        

        view.findViewById<Button>(R.id.button_signin_key).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {                
                Log.d(TAG, "Clicked Login")

                OAuthHelper.buildAuthUrl()?.let {
                    val builder = CustomTabsIntent().Builder()
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(this, it)
                }

            }
        })        

    }
}