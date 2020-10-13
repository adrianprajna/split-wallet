package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.adapter.TransactionAdapter
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig

class TransactionDetailActivity : AppCompatActivity() {

    private var ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER).child(Constants.KEY_USER_ID)
        .child(Constants.LIST_WALLET)
    private lateinit var wallet: WalletsHelper
    private lateinit var day: Number
    private lateinit var month: Number
    private lateinit var year: Number
    private lateinit var transactionList: ArrayList<Transactions>

    private var months = listOf<String>("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")

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
        setContentView(R.layout.activity_transaction_detail)

        day = intent.getStringExtra("day").toString().toInt()
        month = intent.getStringExtra("month").toString().toInt()
        year = intent.getStringExtra("year").toString().toInt()
        wallet = intent.getParcelableExtra("wallet")!!

        date.setText(months[month as Int - 1] + " " + day + ", " + year)
        getAllTransactions()
    }

    private fun getAllTransactions(){

        var dbRef = ref.child(wallet.walletName.toString()).child("transactions")
        var curr = "$year/$month/$day"

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList = ArrayList()

                for(p in snapshot.children){
                    if(p.child("transactionDate").value.toString() == curr)
                        transactionList.add(Transactions(transactionDate = p.child("transactionDate").value.toString(),
                            transactionNote = p.child("transactionNote").value.toString(),
                            transactionAmount = p.child("transactionAmount").value.toString().toInt(),
                            transactionType = p.child("transactionType").value.toString()))
                }

                var adapter = TransactionAdapter(transactionList)
                setListener(adapter)
                recycler_view.adapter = adapter
                recycler_view.layoutManager = LinearLayoutManager(applicationContext)
                recycler_view.setHasFixedSize(true)
            }
        })
    }


    private fun setListener(adapter: TransactionAdapter){
        adapter.setOnItemClickListener(object : TransactionAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                var intent = Intent(this@TransactionDetailActivity, UpdateTransactionActivity::class.java)
                intent.putExtra("transaction", transactionList[position])
                intent.putExtra("walletName", wallet.walletName)
                intent.putExtra("position", position.toString())
                startActivity(intent)
            }
        })
    }


}