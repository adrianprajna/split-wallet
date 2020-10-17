package edu.bluejack20_1.splitwallet

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.whiteelephant.monthpicker.MonthPickerDialog
import edu.bluejack20_1.splitwallet.adapter.TransactionDetailAdapter
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.DateHelper
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlinx.android.synthetic.main.activity_wallet_detail.*
import kotlinx.android.synthetic.main.activity_wallet_detail.recycler_view
import kotlinx.android.synthetic.main.activity_wallet_detail.tempLayout
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.exp
import kotlin.properties.Delegates

class WalletDetailActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private var months = listOf<String>("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
        "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER")

    private lateinit var ref: DatabaseReference
    private lateinit var transactionList: ArrayList<Transactions>
    private lateinit var calendar: Calendar
    private lateinit var adapter: TransactionDetailAdapter
    private lateinit var wallet: Wallets
    private var month = 0
    private var year = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_detail)

        setSupportActionBar(toolbar)
        init()
        getAllData()
    }

    private fun init(){
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        btn_date.setText("${months[month.toInt()]} $year")
        btn_date.setOnClickListener(){
            selectMonth()
        }

        var walletName = intent.getStringExtra("walletName").toString()
        wallet = intent.getSerializableExtra("wallet") as Wallets
        toolbar.title = walletName
        ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
            .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET).child(walletName).child("transactions")
        transactionList = ArrayList()
    }

    private fun getAllData(){
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                var incomes = 0
                var expenses = 0
                for(p in snapshot.children){
                    var splittedDate = DateHelper.splitDate(p.child("transactionDate").value.toString())
                    if(year == splittedDate[0].toInt() && (month + 1) == splittedDate[1].toInt()){
                        var data = Transactions(transactionAmount = p.child("transactionAmount").value.toString().toInt(),
                            transactionNote = p.child("transactionNote").value.toString(),
                            transactionType = p.child("transactionType").value.toString(),
                            transactionDate = p.child("transactionDate").value.toString())

                        transactionList.add(data)

                        if(data.transactionType == "Expense") expenses += data.transactionAmount.toInt()
                        else incomes += data.transactionAmount.toInt()
                    }
                }
                expense.setText("Rp. " + NumberFormat.getIntegerInstance().format(expenses))
                income.setText("Rp. " + NumberFormat.getIntegerInstance().format(incomes))

                var totals = incomes - expenses
                if(totals < 0){
                    total.setText("Rp. " + NumberFormat.getIntegerInstance().format(totals))
                } else {
                    total.setText("+ Rp. " + NumberFormat.getIntegerInstance().format(totals))
                }
                setAdapter()
            }
        })
    }

    private fun setAdapter(){
        adapter = TransactionDetailAdapter(transactionList)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        if(transactionList.isEmpty()){
            tempLayout.visibility = View.VISIBLE
            recycler_view.visibility = View.GONE
        } else {
            tempLayout.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE
        }
    }

    private fun selectMonth(){
        var monthPicker = MonthPickerDialog.Builder(this, object : MonthPickerDialog.OnDateSetListener{
            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                btn_date.setText("${months[selectedMonth]} $selectedYear")
                month = selectedMonth
                year = selectedYear
                getAllData()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))

        monthPicker.setActivatedMonth(calendar.get(Calendar.MONTH))
            .setMinYear(1990)
            .setActivatedYear(calendar.get(Calendar.YEAR))
            .setMaxYear(2025)
            .setTitle("SELECT DATE")
            .build().show()
    }

    fun setPopUpMenu(view: View){
        var popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.sort_menu)
        popup.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit -> {
                var intent = Intent(this, UpdateWalletActivity::class.java)
                intent.putExtra("wallet", wallet)
                intent.putExtra("transactionList", transactionList)
                startActivity(intent)
            }
            R.id.remove -> {
                var alertDialog = MaterialAlertDialogBuilder(this)
                    .setNegativeButton("NO") { dialog, which ->

                    }
                    .setTitle("Are you sure?").setPositiveButton("YES") { dialog, which ->
                        removeWallet()
                    }.show()
            }
    }
        return true
    }

    private fun removeWallet(){
        var dbRef = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
                        .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET).child(wallet.walletName.toString())
        dbRef.removeValue()
        Toast.makeText(this, "Successfully removed this wallet!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun sortByDate(sort: String){
        if(sort == "ASC"){
            transactionList.sortWith(compareBy{
                it.transactionDate
            })
        } else {
            transactionList.sortWith(compareBy{
                it.transactionDate
            })
            transactionList.reverse()
        }

        adapter.notifyDataSetChanged()
    }

    private fun sortByAmount(sort: String){
        if(sort == "ASC"){
            transactionList.sortWith(compareBy {
                it.transactionAmount.toString().toInt()
            })
        } else {
            transactionList.sortWith(compareBy {
                it.transactionAmount.toString().toInt()
            })
            transactionList.reverse()
        }
        adapter.notifyDataSetChanged()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.date_asc -> {
                    sortByDate("ASC")
                    Toast.makeText(this, "DATE ASC", Toast.LENGTH_SHORT).show()
                }
                R.id.date_desc -> {
                    sortByDate("DESC")
                    Toast.makeText(this, "DATE DESC", Toast.LENGTH_SHORT).show()
                }
                R.id.amount_asc -> {
                    sortByAmount("ASC")
                }
                R.id.amount_desc -> {
                    sortByAmount("DESC")
                }
            }
        }

        return true
    }

}