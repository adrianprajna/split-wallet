package edu.bluejack20_1.splitwallet.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.adapter.WalletAdapter
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.DateHelper
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Transaction.newInstance] factory method to
 * create an instance of this fragment.
 */
class Transaction : Fragment(), DatePickerDialog.OnDateSetListener{

    lateinit var inf: View
    private  lateinit var calendar : Calendar
    private var day = 0
    private var month = 0
    private var year = 0

    private lateinit var btn_date: MaterialButton
    private lateinit var walletList : ArrayList<WalletsHelper>
    private lateinit var adapter:  RecyclerView.Adapter<WalletAdapter.WalletViewHolder>
    private lateinit var recyclerView: RecyclerView

    private lateinit var dateFormat: DateFormat


    private var months = listOf<String>("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")

    private var ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER).child(Constants.KEY_USER_ID).
                    child(Constants.LIST_WALLET)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_transaction, container, false)

        calendar = Calendar.getInstance()
        dateFormat = SimpleDateFormat("EEE, MMM d, ''yy")

        btn_date = inf.findViewById(R.id.btn_date)
        btn_date.setText(dateFormat.format(calendar.time))
        btn_date.setOnClickListener(){
            showDatePickerDialog()
        }

        if(activity != null){
            recyclerView = inf.findViewById(R.id.recycler_view)

            var fab = inf.findViewById<FloatingActionButton>(R.id.floating_action_button)
            fab.setOnClickListener(){
                var transactions = ArrayList<Transactions>()
                transactions.add(Transactions("2020/10/10", "Makan bang", 123456, "Income"))
                walletList.add(3, WalletsHelper("Beverage", "Income", 0, transactions))
                adapter.notifyItemInserted(3)
            }


            var date = DateHelper.splitDate(DateHelper.nowToString())
            this.year = date[0].toInt()
            this.month = date[1].toInt()
            this.day = date[2].toInt()
            getData()
        }

        return inf
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        this.day = dayOfMonth
        this.month = month + 1
        this.year = year

        btn_date.setText(dateFormat.format(calendar.time))
        getData()
    }

    private fun showDatePickerDialog(){
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        var datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

    private fun getData(){


        var currDate = "$year/$month/$day"
        walletList = ArrayList()
        ref.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var tempData = snapshot.getValue() as Map<*, *>
                    for( (key, value) in tempData.entries){

                        var data = value as Map<*, *>
                        var tempRef = ref.child(key.toString()).child("transactions")

                        tempRef.addValueEventListener(object : ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {

                                var transactionsList = ArrayList<Transactions>()

                                for(s in snapshot.children){
                                    if(s.child("transactionDate").value.toString() == currDate){
                                        transactionsList.add(Transactions(s.child("transactionDate").value.toString(), s.child("transactionNote").value.toString(),
                                            s.child("transactionAmount").value.toString().toInt(), s.child("transactionType").value.toString()))
                                    }
                                }

                                if(transactionsList.isNotEmpty()){
                                    walletList.add(WalletsHelper(data["walletName"].toString(), data["walletType"].toString(), data["walletLimit"].toString().toInt(),
                                        transactionsList))
                                }

                            }
                        })
                    }

                    val handler = Handler()
                    val delay = 500 //milliseconds

                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            adapter = WalletAdapter(walletList)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.setHasFixedSize(true)
                        }
                    }, delay.toLong())
                }
            }

        })
    }
}


