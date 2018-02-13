package com.star.supernova.like

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import android.widget.Toast
import com.bumptech.glide.Glide
import java.security.MessageDigest
import java.util.*


class FacebookLoginActivity : AppCompatActivity() {

    lateinit var btnLoginFacebook: Button
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)

        // *** Getting KeyHash *** //
//        val info: PackageInfo
//        try {
//            info = packageManager.getPackageInfo("com.star.supernova.like", PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        AppEventsLogger.activateApp(application)
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = AccessToken.getCurrentAccessToken()
                val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->

                    var fbId = ""
                    var fbName = ""
                    var fbEmail = ""
                    var fbGender = ""
                    var fbUrl = ""

                    try {
                        if (`object`.has("id")) {
                            fbId = `object`.getString("id")
                        }
                        if (`object`.has("name")) {
                            fbName = `object`.getString("name")
                        }
                        if (`object`.has("email")) {
                            fbEmail = `object`.getString("email")
                        }
                        if (`object`.has("gender")) {
                            fbGender = `object`.getString("gender")
                        }
                        if (`object`.has("picture")) {
                            fbUrl = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                        }

                        if (fbUrl != "") {
                            var imgFacebookProfile: ImageView = findViewById(R.id.fb_profile_picture)
                            Glide.with(this@FacebookLoginActivity).load(fbUrl).into(imgFacebookProfile)
                        }

                        var txtFacebookData: TextView = findViewById(R.id.fbData)
                        txtFacebookData.text = fbName + "\n" +fbEmail + "\n" +fbGender

                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,email,picture,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                Toast.makeText(this@FacebookLoginActivity, "Cancel", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@FacebookLoginActivity, "onError", Toast.LENGTH_LONG).show()
            }
        })

        btnLoginFacebook = findViewById(R.id.facebook_login_button)

        btnLoginFacebook.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this@FacebookLoginActivity,  Arrays.asList("email", "user_photos", "public_profile"))
        }
    }

    override fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent) {
        super.onActivityResult(requestCode, responseCode, data)
        callbackManager!!.onActivityResult(requestCode, responseCode, data)
    }
}
