package edu.bluejack20_1.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.GsonReader
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlinx.android.synthetic.main.activity_update_wallet.*

class PleaseWaitActivity : AppCompatActivity() {

    var reff = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
                .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_please_wait)


        var wallet = intent.getSerializableExtra("newWallet") as Wallets
        var removedKey = intent.getStringExtra("removedKey").toString()
        var listJson = intent.getStringExtra("transactions")
        var gson = GsonReader()
        var transactionList = gson.getGson().fromJson(listJson, Array<Transactions>::class.java).toList()

        if(wallet.walletType == "Income"){
            reff.child(wallet.walletName.toString()).child("walletType").setValue("Income")
            reff.child(wallet.walletName.toString()).child("walletLimit").setValue(0)
        } else {
            reff.child(wallet.walletName.toString()).child("walletType").setValue("Expense")
            reff.child(wallet.walletName.toString()).child("walletLimit").setValue(wallet_limit_update.text.toString().toInt())
        }
        reff.child(wallet.walletName.toString()).child("walletName").setValue(wallet.walletName.toString())
        reff.child(wallet.walletName.toString()).child("transactions").setValue(transactionList)
    }



}