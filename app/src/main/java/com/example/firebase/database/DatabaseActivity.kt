package com.example.firebase.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase.R
import com.example.firebase.databinding.ActivityDatabaseBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDatabaseBinding
    var rootNode: FirebaseDatabase? = null
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_database)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_database)

        binding.regBtn.setOnClickListener {
            rootNode = FirebaseDatabase.getInstance()
            reference = rootNode!!.getReference("users")
            //Get all the values
            //Get all the values
            val name: String = binding.regName.editText?.text.toString()
            val username: String = binding.regUsername.editText?.text.toString()
            val email: String = binding.regEmail.editText?.text.toString()
            val phoneNo: String = binding.regPhoneNo.editText?.text.toString()
            val password: String = binding.regPassword.editText?.text.toString()
            val helperClass = UserHelperClass(name, username, email, phoneNo, password)
            reference!!.child(phoneNo).setValue(helperClass)
        }
    }
}