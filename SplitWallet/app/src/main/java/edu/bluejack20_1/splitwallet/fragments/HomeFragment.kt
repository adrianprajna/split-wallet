package edu.bluejack20_1.splitwallet.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import edu.bluejack20_1.splitwallet.CreateWalletActivity
import edu.bluejack20_1.splitwallet.LoginActivity
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.SettingActivity
import edu.bluejack20_1.splitwallet.support_class.*
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    var listWallets = arrayListOf<WalletsHelper>()
    var reff = FirebaseDatabase.getInstance().getReference()
        .child(Constants.KEY_USER)

    var totalSpend = 0
    var totalLimit = 0

    lateinit var initViewListener : ValueEventListener

    var reffTransactions : DatabaseReference? = null
    var valueListener: ValueEventListener? = null

    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var inf : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
//        reff.removeEventListener(initViewListener)
//        if (reffTransactions != null){
//            reffTransactions!!.removeEventListener(valueListener!!)
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentManager?.popBackStack()
        inf = inflater.inflate(R.layout.fragment_home, container, false)

        var btn_sign_out = inf.findViewById<Button>(R.id.btn_sign_out)
        btn_sign_out.setOnClickListener(){
            var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
                R.string.default_web_client_id
            )).requestEmail().build()

            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            mGoogleSignInClient.signOut();

            var preferenceConfig = PreferenceConfig(this.requireContext()).clearOneSharedPreference(Constants.KEY_USER)
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        if (activity != null){
            var username_home = inf.findViewById<TextView>(R.id.home_username)
            var email_home = inf.findViewById<TextView>(R.id.home_email)

            var preferenceConfig : PreferenceConfig =
                PreferenceConfig(
                    requireActivity().applicationContext
                )

            val u : Users
            u = preferenceConfig.getGson().fromJson(preferenceConfig.getString(Constants.KEY_USER), Users::class.java)
            username_home.setText(u.username)
            email_home.setText(u.email)

            Constants.KEY_USER_ID = u.email!!.split("@gmail.com")[0]
//            addWallet()


            val btn = inf.findViewById<CardView>(R.id.btn_create_wallet)
            btn.setOnClickListener(){
                var user = Users("asd", "asd", "asd")
                val intent = Intent(activity, CreateWalletActivity::class.java)
                requireActivity().startActivity(intent)
            }

            val setting_btn = inf.findViewById<CardView>(R.id.home_btn_setting)
            setting_btn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val intent = Intent(activity, SettingActivity::class.java)
                    requireActivity().startActivity(intent)
                }

            })

            initView()
        }
        return inf
    }

    fun initView(){


        initViewListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    totalSpend = 0
                    totalLimit = 0
                    collectData(snapshot.getValue() as Map<String, Any>)
                }
            }

        }


        reff = reff.child(Constants.KEY_USER_ID)
            .child(Constants.LIST_WALLET)
        reff.addValueEventListener(initViewListener)

//        changeWalletView()
    }

    fun changeWalletView(){

        var txt_spend = inf.findViewById<TextView>(R.id.home_payment_label)
        txt_spend.text = "Rp. $totalSpend"
        var txt_limit = inf.findViewById<TextView>(R.id.home_limit_label)
        txt_limit.text = "Rp. $totalLimit"

        if (totalSpend < totalLimit/2){
            greenView()
        } else if (totalSpend < totalLimit){
            yellowView()
        } else {
            redView()
        }

    }

    fun greenView(){
        if (isAdded){
            var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
            box.background = resources.getDrawable(R.color.greenHomeCard)
            var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
            tx_desc.setTextColor(resources.getColor(R.color.fontColor))

            var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
            tx_val.setTextColor(resources.getColor(R.color.fontColor))
        }
    }

    fun yellowView(){
        if (isAdded){
            var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
            box.background = resources.getDrawable(R.color.yellowHomeCard)
            var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
            tx_desc.setTextColor(resources.getColor(R.color.fontColor))

            var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
            tx_val.setTextColor(resources.getColor(R.color.fontColor))
        }

    }

    fun redView(){
        if (isAdded){
            var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
            box.background = resources.getDrawable(R.color.redHomeCard)
            var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
            tx_desc.setTextColor(resources.getColor(R.color.fontReverseColor))

            var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
            tx_val.setTextColor(resources.getColor(R.color.fontReverseColor))
        }
    }

    fun collectData(data : Map<String, Any>){


        for ((name, value) in data.entries) {
//                Toast.makeText(this@HomeFragment.context, name, Toast.LENGTH_LONG).show()

                //Get user map
                val singleUser =
                    value as Map<*, *>

                if (singleUser["transactions"] != null) {
                    reffTransactions = reff.child(name).child("transactions")
                    valueListener = object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
//                                var a : ArrayList<Transactions>? =
//                                var a = collectTransaction(snapshot.getValue() as Any)

                            listWallets.add(
                                WalletsHelper(
                                    singleUser["walletName"].toString(),
                                    singleUser["walletType"].toString(),
                                    singleUser["walletLimit"].toString().toInt(),
                                    null
                                )
                            )

                            for (p in snapshot.children){
                                var u =
                                    Transactions(
                                        p.child("transactionDate").value.toString(),
                                        p.child("transactionNote").value.toString(),
                                        p.child("transactionAmount").value.toString().toInt(),
                                        p.child("transactionType").value.toString()

                                    )
                                listWallets.get(listWallets.size - 1).listTransactions.add(u)
                            }

                            if (singleUser["walletType"].toString().equals("Expense")){
                                totalLimit += singleUser["walletLimit"].toString()!!.toInt()
                                for (t in listWallets.get(listWallets.size - 1).listTransactions){
                                    totalSpend += t.transactionAmount!!.toInt()
                                }
                                changeWalletView()
                            }

                        }

                    }
                    reffTransactions!!.addValueEventListener(valueListener!!)
//                    reffTransactions.removeEventListener(valueListener!!)

                } else {
                    listWallets.add(
                        WalletsHelper(
                            singleUser["walletName"].toString(),
                            singleUser["walletType"].toString(),
                            singleUser["walletLimit"].toString().toInt(), null
                        )
                    )
                    if (singleUser["walletType"].toString() == "Expense"){
                        totalLimit += singleUser["walletLimit"].toString()!!.toInt()
                        changeWalletView()
                    }
                }

        }
        if (reffTransactions != null){
            changeWalletView()
        }
        Log.d("Wallet size", listWallets.size.toString())

//        Toast.makeText(this@HomeFragment.context, listWallets.get(0).listTransactions.get(0).toString(), Toast.LENGTH_SHORT).show()
    }


    fun addWallet(){
        var reff = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER).child(
            Constants.KEY_USER_ID).child(
            Constants.LIST_WALLET)



//        val x = mutableMapOf<String, Wallets>()
//
//        x["Food"] = Wallets("Food",
//            "Expense",
//            500000)
//        x["Salary"] = Wallets("Salary",
//            "Income",
//            0)
//
//        reff.setValue(x)


//        reff.child("Electricity").setValue(
//            Wallets(
//                "Electricity",
//                "Expense",
//                2000000
//            )
//        )

        reff.child("Food").setValue(
            Wallets(
                "Food",
                "Expense",
                2000000
            )
        )

        reff.child("Salary").setValue(
            Wallets(
                "Salary",
                "Expense",
                0
            )
        )

//        addTransaction("Electricity")
        addTransaction("Salary")
        addTransaction("Food")
    }

    fun addTransaction(walletName : String){
        var reff = FirebaseDatabase.getInstance().getReference().
        child(Constants.KEY_USER).child(
            Constants.KEY_USER_ID).
        child(Constants.LIST_WALLET).child(walletName)

        var type : String? = null
        reff.child("walletType").addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                type = snapshot.getValue(String::class.java).toString()
                var x = arrayListOf<Transactions>()

                x.add(
                    Transactions(
                        DateHelper.nowToString(),
                        "Elec1",
                        200000,
                        type!!
                    )
                )
                x.add(
                    Transactions(
                        DateHelper.nowToString(),
                        "Elec2",
                        105000,
                        type!!
                    )
                )

                x.add(
                    Transactions(
                        DateHelper.nowToString(),
                        "Elec3",
                        200000,
                        type!!
                    )
                )
                x.add(
                    Transactions(
                        DateHelper.nowToString(),
                        "Elec4",
                        105000,
                        type!!
                    )
                )
                reff.child("transactions").setValue(x)
            }
        })
    }


}