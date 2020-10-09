package edu.bluejack20_1.splitwallet.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.*
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.CreateWalletActivity
import edu.bluejack20_1.splitwallet.LoginActivity
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlinx.android.synthetic.main.fragment_home.*


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

    var totalSpend = 0
    var totalLimit = 0

    lateinit var inf : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_home, container, false)

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
                val intent = Intent(activity, CreateWalletActivity::class.java)
                requireActivity().startActivity(intent)
            }

            initView()
        }

        return inf
    }


    fun initView(){
        var reff = reff.child(Constants.KEY_USER).child(
            Constants.KEY_USER_ID).child(
            Constants.LIST_WALLET)

        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                totalSpend = 0
                totalLimit = 0
                collectData(snapshot.getValue() as Map<String, Any>)
            }

        })

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
        var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
        box.background = resources.getDrawable(R.color.greenHomeCard)
        var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
        tx_desc.setTextColor(resources.getColor(R.color.fontColor))

        var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
        tx_val.setTextColor(resources.getColor(R.color.fontColor))

    }

    fun yellowView(){
        var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
        box.background = resources.getDrawable(R.color.yellowHomeCard)
        var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
        tx_desc.setTextColor(resources.getColor(R.color.fontColor))

        var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
        tx_val.setTextColor(resources.getColor(R.color.fontColor))

    }

    fun redView(){
        var box = inf.findViewById<LinearLayout>(R.id.home_spend_box)
        box.background = resources.getDrawable(R.color.redHomeCard)
        var tx_desc = inf.findViewById<TextView>(R.id.home_payment_desc)
        tx_desc.setTextColor(resources.getColor(R.color.fontReverseColor))

        var tx_val = inf.findViewById<TextView>(R.id.home_payment_label)
        tx_val.setTextColor(resources.getColor(R.color.fontReverseColor))

    }

    fun collectData(data : Map<String, Any>){

            for ((name, value) in data.entries) {
//                Toast.makeText(this@HomeFragment.context, name, Toast.LENGTH_LONG).show()

                //Get user map
                val singleUser =
                    value as Map<*, *>

                if (singleUser["transactions"] != null) {
                    var reffTransactions = reff.child(Constants.KEY_USER).child(
                        Constants.KEY_USER_ID).child(
                        Constants.LIST_WALLET).child(name).child("transactions")

                        reffTransactions.addValueEventListener(object : ValueEventListener {
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

                                    Log.d("children", snapshot.children.toString())
                                    Log.d("children", p.key.toString())

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
                                    Toast.makeText(this@HomeFragment.context, "Sue kong", Toast.LENGTH_LONG).show()
                                    for (t in listWallets.get(listWallets.size - 1).listTransactions){
                                        totalSpend += t.transactionAmount!!.toInt()
                                    }
                                    changeWalletView()

                                }


                            }

                        })
                } else {
                    listWallets.add(
                        WalletsHelper(
                            singleUser["walletName"].toString(),
                            singleUser["walletType"].toString(),
                            singleUser["walletLimit"].toString().toInt(), null
                        )
                    )
                }

        }
        changeWalletView()
//        Toast.makeText(this@HomeFragment.context, listWallets.get(0).listTransactions.get(0).toString(), Toast.LENGTH_SHORT).show()
    }


    fun addWallet(){
        var reff = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER).child(
            Constants.KEY_USER_ID).child(
            Constants.LIST_WALLET)



        val x = mutableMapOf<String, Wallets>()

//        x["Food"] = Wallets("Food",
//            "Expense",
//            500000)
//        x["Salary"] = Wallets("Salary",
//            "Income",
//            0)
//
//        reff.setValue(x)


        reff.child("Electricity").setValue(
            Wallets(
                "Electricity",
                "Expense",
                2000000
            )
        )

        addTransaction("Electricity")
//        addTransaction("Salary")
//        addTransaction("Food")
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
                reff.child("transactions").setValue(x)
            }
        })
    }


}