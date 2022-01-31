package com.example.firebase.signing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.firebase.R
import com.example.firebase.databinding.ActivityGoogleLoginBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class GoogleLogin : AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var binding: ActivityGoogleLoginBinding
    private var auth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var callbackManager: CallbackManager? = null
    private var rcSignIn = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_google_login)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_google_login
        )
        createRequest()
        binding.googleSignin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        auth = FirebaseAuth.getInstance()
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    private fun createRequest() {
        callbackManager = CallbackManager.Factory.create()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("13455078325-0c3b68tol2pkr58rfab3mjo3c8a14m2k.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            try {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result != null) {
                    handleSignInResult(result)
                }
            } catch (e: ApiException) {
                Log.i("======", e.cause.toString() + e.message)
                //Toast.makeText(e.cause.toString() + e.message)
            }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val user = result.signInAccount
            if (user != null) {
                if (!user.displayName.isNullOrEmpty()) {
                    if (user.displayName!!.trim().contains(" ")) {
                        binding.tvName.text = user.displayName
                        binding.tvUserId.text = user.id
                        Glide.with(this).load(user.photoUrl).circleCrop()
                            .into(binding.ivProfilePic)
                    }
                }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}