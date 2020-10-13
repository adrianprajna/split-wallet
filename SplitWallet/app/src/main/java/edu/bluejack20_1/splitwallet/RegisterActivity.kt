package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.Users
import com.google.firebase.database.*
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig

class RegisterActivity : AppCompatActivity() {

    var reff = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER)
    var userList = arrayListOf<Users>()

    lateinit var preferenceConfig : PreferenceConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceConfig =
            PreferenceConfig(
                applicationContext
            )
        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (p in snapshot.children) {
                    var temp = p.getValue(Users::class.java)
                    if (temp != null) {
                        var u = Users(
                            temp.username,
                            temp.password,
                            temp.email,
                        )
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
                Toast.makeText(this,
                    R.string.wrong_confirm_password, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this,
                    R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(username : String, email : String, password : String){

        var users = Users(
            username,
            password,
            email,
        )


        reff.child(users.email!!.split("@gmail.com")[0]).setValue(users)

        Toast.makeText(this,
            R.string.success_register_toast, Toast.LENGTH_SHORT).show()
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun checkValid(email: String, username: String, password: String) : Boolean {
        if (!email.startsWith("@") && email.endsWith("@gmail.com")){
            if (password.length >= 6 && isAlphanumeric(password)){
                for (p in userList){
                    if (p.email.equals(email)){
                        var txt_email = findViewById<TextView>(R.id.email)
                        txt_email.error = "Email already exists!"
                        return false
                    } else if(p.username.equals(username)){
                        var txt_username = findViewById<TextView>(R.id.username)
                        txt_username.error = "Username already exists!"
                        return false
                    }
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