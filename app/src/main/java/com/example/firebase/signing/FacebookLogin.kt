package com.example.firebase.signing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.firebase.R
import com.example.firebase.databinding.ActivityFacebookLoginBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException

class FacebookLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFacebookLoginBinding
    private var callbackManager: CallbackManager? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_facebook_login)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_facebook_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()


        binding.btnFbLogin.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    getFbUserDetail(loginResult.accessToken)
                }

                override fun onCancel() {
                    //context.toast(getString(R.string.sign_in_cancel))
                }

                override fun onError(error: FacebookException) {
                    AccessToken.getCurrentAccessToken()?.let {
                        LoginManager.getInstance().logOut()
                    }
                    error.message?.let {
                        //context.toast(it, false)
                    }
                }
            })
        }
    }

    private fun getFbUserDetail(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            currentAccessToken
        ) { `object`, response ->
            var firstname = ""
            var lastname = ""
            var email = ""
            var picture = ""
            var dob = ""
            try {
                val name = `object`.getString("name")
                binding.tvName.text = name
                val id = `object`.getString("id")
                binding.tvUserId.text = id
                if (`object`.has("email")) {
                    email = `object`.getString("email")
                }
                if (`object`.has("picture")) {
                    //val obj `object`.has("picture")
                    picture =
                        `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                    //  val   picture"https://graph.facebook.com/${id}/picture?type=square"
                    Glide.with(this).load(picture).circleCrop()
                        .into(binding.ivProfilePic)
                }
                if (`object`.has("user_birthday")) {
                    dob = `object`.getString("user_birthday")
                }

                if (name.contains(" ")) {
                    val list: List<String> = name.split(" ")
                    firstname = list[0]
                    lastname = list[1]

                } else {
                    firstname = name
                }
                /*context.toast("email :$email,\n firstName: $firstname,\n" +
                        " lastName: $lastname, \n picture : $dob")*/
            } catch (e: JSONException) {
                e.printStackTrace()
                /* context.toast("email :$email,\n firstName: $firstname,\n" +
                         " lastName: $lastname,\n picture : $dob")*/
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, email, picture")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}