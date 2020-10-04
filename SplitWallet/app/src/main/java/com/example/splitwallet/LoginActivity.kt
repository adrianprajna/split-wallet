package com.example.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkLogin()
        clickRegister()

    }

    fun checkLogin(){

        var btn_login = findViewById<Button>(R.id.loginButton)

        btn_login.setOnClickListener(){
            var et_username = findViewById<EditText>(R.id.usernameLogin).text
            var et_password = findViewById<EditText>(R.id.passwordLogin).text

            if (!et_username.isEmpty() && !et_password.isEmpty()){
                Toast.makeText(this, R.string.success_login_toast, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun clickRegister(){
        var text_register = findViewById<TextView>(R.id.registerHyperlink)


        text_register.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent);
        }
    }

}