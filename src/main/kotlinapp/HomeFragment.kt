//
//  HomeFragment.kt
//  Veracoin
//

package kotlinapp

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log

import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.Fragment

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient

import firebase.FirebaseAnalytics

class HomeFragment : BaseLoggingFragment() {

    companion object {
        private val TAG: String? = HomeFragment::class.simpleName
    }

    private var balanceTextView:TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, 
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.balanceTextView = view.findViewById<TextView>(R.id.textview_balance)

        view.findViewById<Button>(R.id.button_atm_withdraws).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Router.showWithdraws()
            }
        })      
    }

    override fun onStart() {
        super.onStart()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            // Got last known location. In some rare situations this can be null.
            location?.also { loc ->
                VeracoinAPI.reportPosition(loc)
            }

        }

        val currentUser = UserManager.currentUser
        if (currentUser != null) {
            val balance = currentUser.balance.toString()
            this.balanceTextView?.setText(balance)
        }
    }

}