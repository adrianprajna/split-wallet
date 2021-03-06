package edu.bluejack20_1.splitwallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.gson.reflect.TypeToken
import edu.bluejack20_1.splitwallet.support_class.*
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import kotlinx.android.synthetic.main.activity_update_wallet.*

class UpdateWalletActivity : AppCompatActivity() {

    private var flag = false
    private lateinit var transactionList: ArrayList<Transactions>
    private lateinit var wallet: Wallets
    private lateinit var reff: DatabaseReference

    private lateinit var dbRef: DatabaseReference

    private lateinit var preferenceConfig : PreferenceConfig

    private var refListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                if(snapshot.child(Constants.capitalizeEachWord(wallet_name_update.text.toString())).exists() && wallet_name_update.text.toString() != wallet.walletName){
                    wallet_name_update.setError(getString(R.string.wallet_available))
                } else {
                    remove()
                }
            }
        }
    }


    private fun remove(){
        var temp: WalletsHelper
        reff.child(wallet.walletName.toString()).removeValue().addOnCompleteListener {
            if(autoComplete.text.toString() == getString(R.string.incomes)){
                for(i in transactionList){
                    i.transactionType = "Income"
                }
                temp = WalletsHelper(wallet_name_update.text.toString(), "Income",  0, transactionList)
            } else {
                for(i in transactionList){
                    i.transactionType = "Expense"
                }
                temp = WalletsHelper(wallet_name_update.text.toString(), "Expense",  wallet_limit_update.text.toString().toInt(), transactionList)
            }
            reff.child(wallet_name_update.text.toString()).setValue(temp)
            Toast.makeText(applicationContext, getString(R.string.update_wallet_success), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@UpdateWalletActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceConfig =
            PreferenceConfig(
                this
            )
        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_wallet)


        initItem()
        addAction()
    }

    fun initItem(){
        val items = listOf<String>(getString(R.string.expenses), getString(R.string.incomes))
        val adapter = ArrayAdapter(this, R.layout.list_item, items)

        wallet = intent.getSerializableExtra("wallet") as Wallets
        Log.d("test", wallet.walletName.toString())
        wallet_name_update.setText(wallet.walletName)

        transactionList = intent.getSerializableExtra("transactionList") as ArrayList<Transactions>
        if(wallet.walletType == "Expense"){
            wallet_limit_update.setText(wallet.walletLimit.toString())
            autoComplete.setText(getString(R.string.expenses))
            limit_layout.visibility = View.VISIBLE
        } else {
            autoComplete.setText(getString(R.string.incomes))
            limit_layout.visibility = View.GONE
        }
        autoComplete.setAdapter(adapter)

        btn_update_wallet.setOnClickListener(){
            updateWallet()
        }


        reff = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
            .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)
        dbRef = reff.child(wallet.walletName.toString()).child("transactions")
    }

    fun addAction(){
        autoComplete.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(autoComplete.text.toString() == getString(R.string.incomes)){
                    limit_layout.visibility = View.GONE
                } else {
                    limit_layout.visibility = View.VISIBLE
                }
            }
        })
    }

    fun validateWalletName(): Boolean{

        if (wallet_name_update.text.toString().isEmpty()) {
            wallet_name_update.setError(getString(R.string.wallet_empty))
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        reff.removeEventListener(refListener)
    }

    private fun updateWallet(){
        if(!validateWalletName()){
            return
        }

        if(autoComplete.text.toString().isEmpty()){
            Toast.makeText(this, getString(R.string.input_wallet_type), Toast.LENGTH_SHORT).show()
            return
        }

        if(autoComplete.text.toString() == getString(R.string.expenses) && (wallet_limit_update.text.toString().isEmpty() || wallet_limit_update.text.toString().toInt() <= 0)){
            wallet_limit_update.setError(getString(R.string.limit_greater))
            return
        }

        reff.addListenerForSingleValueEvent(refListener)

    }

}