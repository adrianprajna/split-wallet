package edu.bluejack20_1.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.Transactions
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.android.synthetic.main.activity_update_transaction.*

class UpdateTransactionActivity : AppCompatActivity() {

    private var ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
        .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)
    private lateinit var walletName: String
    private lateinit var transaction: Transactions
    private lateinit var position: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_transaction)


        transaction = intent.getSerializableExtra("transaction") as Transactions
        walletName = intent.getStringExtra("walletName").toString()
        position = intent.getStringExtra("position").toString()
        transaction_note.setText(transaction.transactionNote)
        transaction_amount.setText(transaction.transactionAmount.toString())

        setListener()
    }


    private fun setListener(){
        btn_update.setOnClickListener(){
            updateTransaction()
        }
    }

    private fun updateTransaction(){
        var dbRef = ref.child(walletName).child("transactions")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(p in snapshot.children){
                    if(p.child("transactionAmount").value.toString().toInt() == transaction.transactionAmount
                        && p.child("transactionNote").value.toString() == transaction.transactionNote
                        && p.child("transactionDate").value.toString() == transaction.transactionDate){

                        var tempRef = dbRef.child(p.key.toString())
                        tempRef.child("transactionNote").setValue(transaction_note.text.toString())
                        tempRef.child("transactionAmount").setValue(transaction_amount.text.toString().toInt())
                        Toast.makeText(this@UpdateTransactionActivity, "Successfully updated this transaction", Toast.LENGTH_SHORT).show()
                        finish()
                        break
                    }
                }
            }
        })
    }
}