package com.example.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var reff = FirebaseDatabase.getInstance().getReference().child("Users")
    var userList = arrayListOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reff = FirebaseDatabase.getInstance().getReference().child("Users")

        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (p in snapshot.children){
                    var temp = p.getValue(Users::class.java)
                    if (temp != null) {
                        var u = Users(temp.username, temp.password, temp.email)
                        userList.add(u)
                    }
                }


            }

        })



        checkRegister()
    }

    fun checkRegister(){
        var btn_login = findViewById<Button>(R.id.loginButton)

        btn_login.setOnClickListener(){
            var et_username = findViewById<EditText>(R.id.username).text.toString()
            var et_email = findViewById<EditText>(R.id.email).text.toString()
            var et_password = findViewById<EditText>(R.id.passwordLogin).text.toString()
            var et_re_password = findViewById<EditText>(R.id.confirmLogin).text.toString()

            if (!et_username.isEmpty() && !et_password.isEmpty() && !et_email.isEmpty() && et_password.equals(et_re_password)){
                if (checkValid(et_email, et_username, et_password)){
                    createUser(et_username, et_email, et_password)
                }
            } else if (!et_password.equals(et_re_password) && !et_re_password.isEmpty()){
                Toast.makeText(this, R.string.wrong_confirm_password, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(username : String, email : String, password : String){

        var users = Users(username, password, email)
        reff.push().setValue(users)

        Toast.makeText(this, R.string.success_register_toast, Toast.LENGTH_SHORT).show()
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun checkValid(email: String, username: String, password: String) : Boolean {
        if (!email.startsWith("@") && email.endsWith("@gmail.com")){
            if (password.length >= 6 && isAlphanumeric(password)){
                for (p in userList){
                    if (p.username.equals(username) || p.email.equals(email)) return false
                }
                return true
            }
        }
        return false
    }

    fun isAlphanumeric(password: String) : Boolean {
        var len = password.length
        var i = 0
        var isAlpha = false
        var isNumeric = false
        while(i < len){
            if ((password[i] in 'a'..'z') || password[i] in 'A'..'Z'){
                isAlpha = true
            } else if ((password[i] in '0'..'9')){
                isNumeric = true
            } else {
                return false
            }
            i++
        }

        if (isAlpha && isNumeric) return true
        return false
    }


}