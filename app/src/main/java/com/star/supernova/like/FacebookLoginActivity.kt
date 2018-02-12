package com.star.supernova.like

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import org.json.JSONObject
import java.util.*
import com.facebook.GraphResponse
import com.facebook.GraphRequest



class FacebookLoginActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)
        AppEventsLogger.activateApp(application)

        var btnLoginFacebook = findViewById<Button>(R.id.facebook_login_button)

        callbackManager = CallbackManager.Factory.create()

        btnLoginFacebook.setOnClickListener(View.OnClickListener {
            callbackManager = CallbackManager.Factory.create()

            LoginManager.getInstance().logInWithReadPermissions(this,  Arrays.asList("public_profile", "email", "user_birthday"))

            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            Log.d("FacebookLoginActivity", "Facebook token: " + loginResult.accessToken.token)
                            GraphRequest.newMeRequest(loginResult.accessToken) {
                                obj, response ->
                                Log.d("Response", response.toString())

//                                // Application code
//                                val publicProfile = obj.getString("public_profile")
//                                val email = obj.getString("email")
//                                val birthday = obj.getString("birthday")
                            }
                        }

                        override fun onCancel() {
                            Log.d("FacebookLoginActivity", "Facebook onCancel")
                        }

                        override fun onError(exception: FacebookException) {
                            Log.d("FacebookLoginActivity", "Facebook onError")
                        }
                    })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}
