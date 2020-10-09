package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import edu.bluejack20_1.splitwallet.R.layout
import kotlinx.android.synthetic.main.activity_create_wallet.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.*
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.Wallets
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper

class CreateWalletActivity : AppCompatActivity() {

    val reff = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
        .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)

    var flag : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_create_wallet)
        initItem()
        addAction()
    }

    fun initItem(){

        val items = listOf<String>("Expense", "Income")
        val adapter = ArrayAdapter(this, layout.list_item, items)
        autoComplete.setAdapter(adapter)
    }

    fun addAction(){
        autoComplete.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                wallet_limit.isEnabled = !autoComplete.text.toString().equals("Income")
            }
        })
    }

    fun validateWalletName(): Boolean{

        flag = true

        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    if(snapshot.child(Constants.capitalizeEachWord(wallet_name.text.toString())).exists()){
                        wallet_name.setError("Wallet name is exists!")
                        flag = false
                    }
                }
            }

        })

        return if (wallet_name.text.toString().isEmpty()) {
            wallet_name.setError("Field can't be empty")
            false
        }
        else {
            wallet_name.setError(null)
            true
        }
    }


    fun addWallet(view: View){
        if(!validateWalletName()){
            return
        }

        if(autoComplete.text.toString().isEmpty()){
            Toast.makeText(this, "You have to choose the wallet type!", Toast.LENGTH_SHORT).show()
            return
        }

        reff.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var list = mutableMapOf<String, Any>()
                if (snapshot.exists()){
                    list = (snapshot.getValue() as Map<String, Any>).toMutableMap()

                    if(autoComplete.text.toString().equals("Income")){
                        list[wallet_name.text.toString()] = Wallets(
                            walletName = Constants.capitalizeEachWord(wallet_name.text.toString()),
                            walletType = autoComplete.text.toString(),
                            walletLimit = 0
                        )
                    } else {
                        list[wallet_name.text.toString()] = Wallets(
                            walletName = Constants.capitalizeEachWord(wallet_name.text.toString()),
                            walletType = autoComplete.text.toString(),
                            walletLimit = wallet_limit.text.toString().toInt()
                        )
                    }
                    if(flag){
                        reff.setValue(list)
                        Toast.makeText(this@CreateWalletActivity, "Success create new wallet!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateWalletActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    if(autoComplete.text.toString().equals("Income")){
                        list[wallet_name.text.toString()] = Wallets(
                            walletName = Constants.capitalizeEachWord(wallet_name.text.toString()),
                            walletType = autoComplete.text.toString(),
                            walletLimit = 0
                        )
                    } else {
                        list[wallet_name.text.toString()] = Wallets(
                            walletName = Constants.capitalizeEachWord(wallet_name.text.toString()),
                            walletType = autoComplete.text.toString(),
                            walletLimit = wallet_limit.text.toString().toInt()
                        )
                    }
                    reff.setValue(list)
                    Toast.makeText(this@CreateWalletActivity, "Success create new wallet!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CreateWalletActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

}