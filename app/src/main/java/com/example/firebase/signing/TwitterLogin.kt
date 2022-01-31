package com.example.firebase.signing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.firebase.R
import com.example.firebase.databinding.ActivityTwitterLoginBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient

class TwitterLogin : AppCompatActivity() {

    private lateinit var binding : ActivityTwitterLoginBinding
    private var callbackManager: CallbackManager? = null
    private var auth: FirebaseAuth? = null
    private val consumerKey = "BY2YqZxx1JoOjEQzpMvFLV91B"
    private val consumerSecreteKey = "zbIcdkkL8VTTGU9967R7kExwlPqAnucgKzczwyUq7d9Pl5UDwE"
    private var mTwitterAuthClient: TwitterAuthClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_twitter_login)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_twitter_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    consumerKey,
                    consumerSecreteKey
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)
        binding.tvTwitterSignin.setOnClickListener {
            twitterLogin()
        }
        mTwitterAuthClient = TwitterAuthClient()
    }

    private fun getTwitterSession(): TwitterSession? {
        return TwitterCore.getInstance().sessionManager.activeSession
    }

    private fun twitterLogin() {
        if (getTwitterSession() == null) {
            mTwitterAuthClient!!.authorize(this, object : Callback<TwitterSession>() {
                override fun success(twitterSessionResult: Result<TwitterSession>) {
                    Toast.makeText(this@TwitterLogin, "Success", Toast.LENGTH_SHORT).show()
                    val twitterSession = twitterSessionResult.data
                    fetchTwitterEmail(twitterSession)
                }
                override fun failure(e: TwitterException) {
                    Toast.makeText(this@TwitterLogin, "Failure $e", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            fetchTwitterEmail(getTwitterSession())
        }
    }

    fun fetchTwitterEmail(twitterSession: TwitterSession?) {
        mTwitterAuthClient?.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                Log.d("TAG", "twitterLogin:userId" + twitterSession!!.userId)
                Log.d("TAG", "twitterLogin:userName" + twitterSession.userName)
                Log.d("TAG", "twitterLogin: result.data" + result.data)
                val username = twitterSession.userName
                val token = twitterSession.userId.toString()
                var str = "Now you are successfully login with twitter \n\n"
                var tokenStr = ""
                var usernameStr = ""
                val emailStr = ""
                if (token != null || token != "") {
                    tokenStr = "User Id : $token\n\n"
                }
                if (username != null || username != "") {
                    usernameStr = "Username : $username\n\n"
                }
                binding.tvName.text = username
                binding.tvUserId.text = twitterSession.userId.toString()
                /*Glide.with(this).load(twitterSession.).circleCrop()
                    .into(binding.ivProfilePic)*/
            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(
                    this@TwitterLogin,
                    "Failed to authenticate. Please try again.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (mTwitterAuthClient != null)
            mTwitterAuthClient!!.onActivityResult(requestCode, resultCode, data)
    }
}