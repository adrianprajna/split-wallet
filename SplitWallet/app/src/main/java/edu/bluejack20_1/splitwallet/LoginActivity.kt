package edu.bluejack20_1.splitwallet

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users


class LoginActivity : AppCompatActivity() {

    var RC_SIGN_IN = 1
    var mAuth = FirebaseAuth.getInstance()
    lateinit var reff : DatabaseReference
    var userList = arrayListOf<Users>()
    lateinit var preferenceConfig : PreferenceConfig


    lateinit var mGoogleSignInClient : GoogleSignInClient
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
        setContentView(R.layout.activity_login)

        var user_storage : String? = preferenceConfig.getString(Constants.KEY_USER)

        if (user_storage != null){
//            preferenceConfig.clearSharedPreference()
            preferenceLogin(user_storage)
        } else {
            normalLogin()
        }

    }

    fun preferenceLogin(user: String){
        var u = preferenceConfig.getGson().fromJson(user, Users::class.java)
        Toast.makeText(this, "Welcome " + u.username, Toast.LENGTH_SHORT).show()
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    var exit = false


    @Override
    override fun onBackPressed() {
        if (exit){
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed(Runnable { exit = false }, 3 * 1000)
        }
    }

    fun normalLogin(){
        reff = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER)

        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (p in snapshot.children){
                    var temp = p.getValue(Users::class.java)
//                    if (temp != null) {
//                        var u = Users(
//                            temp.username,
//                            temp.password,
//                            temp.email,
//                            null
//                        )
//                        userList.add(u)
//                    }
                    userList.add(temp!!)
                }
            }
        })

        checkLogin()
        clickRegister()
        loginWithGoogle()
    }



    fun checkLogin(){

        var btn_login = findViewById<Button>(R.id.loginButton)

        btn_login.setOnClickListener(){
            var et_username = findViewById<EditText>(R.id.usernameLogin).text
            var et_password = findViewById<EditText>(R.id.passwordLogin).text

            if (!et_username.isEmpty() && !et_password.isEmpty()){
                var userValid = false
                var u : Users?
                u = null
                for(p in userList){
                    if (et_username.toString() == p.username){
                        userValid = true
                        u = p
                        break
                    }


                }

                if (userValid){
                    if (u != null){
                        if (u.password!!.equals(et_password.toString())){
                            successLogin(u)
                        }
                        else {
                            Toast.makeText(this,
                                R.string.wrong_credential, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this,
                        R.string.username_invalid, Toast.LENGTH_SHORT).show()
                }
//                reff.child("ferdinandg066").addValueEventListener(object : ValueEventListener {
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//
//                        var temp = snapshot.getValue(Users::class.java)
//                        var us : Users
//                        if (temp != null){
//                            us = Users(
//                                temp.username,
//                                temp.password,
//                                temp.email,
//                                null)
//                            Toast.makeText(this@LoginActivity, us.username, Toast.LENGTH_SHORT).show()
//
//                        }
//
//                    }
//
//                })

            } else {
                Toast.makeText(this,
                    R.string.empty_field_toast, Toast.LENGTH_SHORT).show()
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

    fun loginWithGoogle(){
        var signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
            R.string.default_web_client_id
        )).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)




        signInButton.setOnClickListener(){
            var signInIntent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
//        invalidGoogleSignIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult(completedTask : Task<GoogleSignInAccount>){
        try {
            var acc = completedTask.getResult(ApiException::class.java)
            Toast.makeText(this, "Sign In Success", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAuth(acc);
        }
        catch(e : ApiException){
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAuth(null);
        }

    }

    private fun FirebaseGoogleAuth(acct: GoogleSignInAccount?) {
        //check if the account is null
        if (acct != null) {
            val authCredential =
                GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = mAuth.getCurrentUser()
                            updateUI(user)
                        } else {
                            updateUI(null)
                        }
                    })
        } else {
            Toast.makeText(this, "acc failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(fUser: FirebaseUser?) {
//        btnSignOut.setVisibility(View.VISIBLE)
        val account =
            GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personFamilyName = account.familyName
            val personEmail = account.email
            val personId = account.id
            val personPhoto: Uri? = account.photoUrl

            var userValid = false
            var u : Users?
            u = null
            for(p in userList){
                if (personEmail.toString().equals(p.email)){
                    userValid = true
                    u = p

                    break
                }


            }

            if (userValid){
                if (u != null){
                    successLogin(u)
                }
            } else {
                invalidGoogleSignIn()
            }

        }
    }

    fun invalidGoogleSignIn(){
        mGoogleSignInClient.signOut();
        Toast.makeText(this,"Please register your email first",Toast.LENGTH_SHORT).show();
    }

    fun successLogin(u : Users){
        preferenceConfig =
            PreferenceConfig(
                applicationContext
            )
        preferenceConfig.putString(Constants.KEY_USER, preferenceConfig.listToJson(u))

        if (preferenceConfig.getString(Constants.THEME_STATUS) == null){
            preferenceConfig.putString(Constants.THEME_STATUS, Constants.THEME_LIGHT)
        }

        Toast.makeText(this, "Welcome " + u.username, Toast.LENGTH_SHORT).show()
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}