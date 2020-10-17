package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var reff : DatabaseReference
    lateinit var reffListener : ValueEventListener

    lateinit var userReff : DatabaseReference
    lateinit var userReffListener: ValueEventListener
    
    var hasEventListener = false;

    lateinit var preferenceConfig : PreferenceConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        getUser()
    }

    fun getUser(){
        reff = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER).child(Constants.KEY_USER_ID)
        var u : Users
        reffListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                u = Users(username = snapshot.child("username").value.toString(),
                    email = snapshot.child("email").value.toString(), password = snapshot.child("password").value.toString())
                clickButton(u)
            }

        }
        reff.addValueEventListener(reffListener)

    }

    fun clickButton(u : Users){
        email.setText(u.email)
        username.setText(u.username)
        passwordLogin.setText(u.password)
        confirmLogin.setText(u.password)
        loginButton.setOnClickListener {
            var _username = username.text.toString()
            var _password = passwordLogin.text.toString()
            var _confirmPass = confirmLogin.text.toString()
            
            if (hasEventListener){
                userReff.removeEventListener(userReffListener)
            }
            if (_password != _confirmPass){
                Toast.makeText(this, R.string.wrong_confirm_password, Toast.LENGTH_SHORT).show()
            } else if (_password.length >= 6 && isAlphanumeric(_password)){
                var us : Users
                us = Users(username = _username, password = _password, email = email.text.toString())
                updateUser(us)
            } else {
                Toast.makeText(this, R.string.wrong_validation_password, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isAlphanumeric(password : String) : Boolean{
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

    fun updateUser(u : Users){
        userReff = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
        userReffListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var flag = true;
                for (p in snapshot.children) {
                    var temp = p.getValue(Users::class.java)
                    if (temp != null) {
                        if (temp.username == u.username) flag = false;
                    }
                }

                if (flag) replaceData(u)
                else
                    Toast.makeText(this@EditProfileActivity, R.string.username_invalid, Toast.LENGTH_SHORT).show()

            }
        }
        userReff.addValueEventListener(userReffListener)

    }

    fun replaceData(u : Users){
        userReff.removeEventListener(userReffListener)
        reff.child("username").setValue(u.username)
        reff.child("password").setValue(u.password)
        hasEventListener = true

        preferenceConfig =
            PreferenceConfig(
                applicationContext
            )
        preferenceConfig.putString(Constants.KEY_USER, preferenceConfig.listToJson(u))
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}