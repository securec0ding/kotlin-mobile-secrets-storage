//
// App.kt
// Veracoin 
//
//  This is support code for the Mock Mobile Development environment
//  You should not revise this code or it may break the lesson.
//
// Driver program that communicates with mock phone via Websockets
//

package kotlinapp

import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import android.widget.EditText

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ServerHandshake

import java.net.URI;
import java.net.URISyntaxException
import java.nio.ByteBuffer;

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*

class NativeWebSocketClient : WebSocketClient {
    var running = false
    var connected = false
    val signinFragment = SigninFragment()
    val homeFragment = HomeFragment()
    val withdrawsFragment = WithdrawsFragment()

    constructor(serverUri: URI?, draft: Draft?) : super(serverUri, draft) {}
    constructor(serverURI: URI?) : super(serverURI) {}

    override fun onOpen(handshakedata: ServerHandshake) {
        connected = true
        send("C|native")
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        connected = false
    }

    override fun onMessage(message: String) {
        //print("M: $message\n")
        val msgParts = message.split("|")
        if (msgParts.size == 2 && msgParts[0] == "C" && msgParts[1] == "launch") {
            if (!running) {
                //gMainActivity.intent = Intent()
                gMainActivity.onCreate(null)
                //let _ = appDelegate.application(app, didFinishLaunchingWithOptions: [:])
                running = true
            } else {
                //print("App alreading running, entering foreground")
            }

            signinFragment.internalCreateView()

            return
        }

        if (msgParts.size == 2 && msgParts[0] == "U")  {
            //gMainActivity.intent = Intent()
            gMainActivity.getIntent().action = Intent.ACTION_VIEW
            gMainActivity.getIntent().setData(Uri(msgParts[1]))
            gMainActivity.onCreate(null)
            if (!running) {
                running = true
            } 
            send("L|past url")
            return
        }

        if (msgParts.size == 3 && msgParts[0] == "L")  {
            if (!running) {
                //gMainActivity.intent = Intent()
                gMainActivity.onCreate(null)
            }
            running = true
            
            val view = signinFragment.getView()

            if (view != null) {            
                val emailEditText = view.findViewById<EditText>(R.id.edittext_email)
                emailEditText.mockSetText(msgParts[1])
                
                val passwordEditText = view.findViewById<EditText>(R.id.edittext_password)
                passwordEditText.mockSetText(msgParts[2])

                val signinButton = view.findViewById<Button>(R.id.button_signin)
                signinButton.internalTriggerClick()
            }

            return
        }        

        if (msgParts.size == 2 && msgParts[0] == "V" && msgParts[1] == "M")  {
            homeFragment.internalCreateView()

            return
        }        

        if (msgParts.size == 2 && msgParts[0] == "V" && msgParts[1] == "W")  {
            withdrawsFragment.internalCreateView()

            return
        }        

        // Trigger Sign in with OAuth on Login Screen
        if (msgParts.size == 1 && msgParts[0] == "K")  {
            val view = signinFragment.getView()

            if (view != null) {            
                val signinButtonKey = view.findViewById<Button>(R.id.button_signin_key)
                signinButtonKey.internalTriggerClick()
            }
        }

        // OAuth Login Screen
        if (msgParts.size == 2 && msgParts[0] == "WL")  {
            //println("WL ==> $msgParts[1]")
            
            var redirectActivity = RedirectUriReceiverActivity()
            redirectActivity.trigger(msgParts[1]);
        }

        // Error from OAuth
        if (msgParts.size == 2 && msgParts[0] == "WE")  {
            //println("WE ==> $msgParts[1]")

            var redirectActivity = RedirectUriReceiverActivity()
            redirectActivity.triggerError(msgParts[1]);
        }

        // ATM Withdraw
        if (msgParts.size == 1 && msgParts[0] == "ATM")  {
            val view = homeFragment.getView()

            if (view != null) {            
                val atmButtonKey = view.findViewById<Button>(R.id.button_atm_withdraws)
                atmButtonKey.internalTriggerClick()
            }
        }

    }

    override fun onMessage(message: ByteBuffer?) {
        //println("received ByteBuffer")
    }

    override fun onError(ex: Exception) {
        connected = false
        System.err.println("an error occurred:$ex")
    }
}

var gWebSocket: NativeWebSocketClient? = null
val gMainActivity = MainActivity()

fun main()
{
    var vport_str: String = System.getenv("VIRTUAL_PORT") ?: "35000"

    Router.showLogin()

    while (true) {
        val client = NativeWebSocketClient(URI("ws://localhost:" + vport_str))
        client.connect()
        gWebSocket = client
        while (true) {
            Thread.sleep(2000L)
            if (!client.connected) {
                break
            }
        }
    }
}