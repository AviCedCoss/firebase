package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.firebase.database.DatabaseActivity
import com.example.firebase.signing.FacebookLogin
import com.example.firebase.signing.GoogleLogin
import com.example.firebase.signing.TwitterLogin


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        binding.btnDatabase.setOnClickListener {
            val myIntent = Intent(this, DatabaseActivity::class.java)
            this.startActivity(myIntent)
        }
        binding.btnGoogleLogin.setOnClickListener {
            val myIntent = Intent(this, GoogleLogin::class.java)
            this.startActivity(myIntent)
        }
        binding.btnTwitterLogin.setOnClickListener {
            val myIntent = Intent(this, TwitterLogin::class.java)
            this.startActivity(myIntent)
        }
        binding.btnFacebookLogin.setOnClickListener {
            val myIntent = Intent(this, FacebookLogin::class.java)
            this.startActivity(myIntent)
        }


    }
}