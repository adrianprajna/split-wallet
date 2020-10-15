package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlinx.android.synthetic.main.activity_create_transaction.*

class CreateTransaction : AppCompatActivity() {

    private var ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
        .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)
    private lateinit var listWallets: ArrayList<String>
    private lateinit var day: Number
    private lateinit var month: Number
    private lateinit var year: Number


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_transaction)

        init()
        getAllWallets()
        setListener()
    }

    private fun init(){
        day = intent.getStringExtra("day").toString().toInt()
        month = intent.getStringExtra("month").toString().toInt()
        year = intent.getStringExtra("year").toString().toInt()
    }

    private fun getAllWallets(){

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listWallets = ArrayList()
                var data = snapshot.getValue() as Map<*, *>
                for((key, value) in data.entries){
                    listWallets.add(key.toString())
                }

                var adapter = ArrayAdapter(this@CreateTransaction, R.layout.list_item, listWallets)
                autoComplete.setAdapter(adapter)
            }
        })
    }

    private fun setListener(){
        btn_add.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                getType()
            }
        })
    }

    private fun getType(){
        var type = ref.child(autoComplete.text.toString()).child("walletType")

        type.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                addTransaction(snapshot.value.toString())
            }

        })

    }

    private fun addTransaction(type: String){
        var dbRef = ref.child(autoComplete.text.toString()).child("transactions")
        var date = "$year/$month/$day"


        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var t = Transactions(transactionDate = date, transactionType = type,
                    transactionAmount = transaction_amount.text.toString().toInt(), transactionNote = transaction_note.text.toString())

                dbRef.child(snapshot.childrenCount.toString()).setValue(t)
                Toast.makeText(this@CreateTransaction, "Successfully added a new transaction!", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@CreateTransaction, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}