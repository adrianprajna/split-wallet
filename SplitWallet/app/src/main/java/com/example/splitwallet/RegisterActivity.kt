package com.example.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        checkRegister()
    }

    fun checkRegister(){
        var btn_login = findViewById<Button>(R.id.loginButton)

        btn_login.setOnClickListener(){
            var et_username = findViewById<EditText>(R.id.username).text
            var et_email = findViewById<EditText>(R.id.email).text
            var et_password = findViewById<EditText>(R.id.passwordLogin).text

            if (!et_username.isEmpty() && !et_password.isEmpty() && !et_email.isEmpty()){
                Toast.makeText(this, R.string.success_register_toast, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

}