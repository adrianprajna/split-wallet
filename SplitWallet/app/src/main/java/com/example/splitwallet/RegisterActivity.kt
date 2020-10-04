package com.example.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        checkRegister()
    }

    fun checkRegister(){
        var btn_login = findViewById<Button>(R.id.loginButton)

        btn_login.setOnClickListener(){
            var et_username = findViewById<EditText>(R.id.username).text.toString()
            var et_email = findViewById<EditText>(R.id.email).text.toString()
            var et_password = findViewById<EditText>(R.id.passwordLogin).text.toString()

            if (!et_username.isEmpty() && !et_password.isEmpty() && !et_email.isEmpty()){
                createUser(et_username, et_email, et_password)
            } else {
                Toast.makeText(this, R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(username : String, email : String, password : String){
        var reff : DatabaseReference
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

        var users = Users(username, password, email)
        reff.push().setValue(users)
    }


}