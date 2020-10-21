package edu.bluejack20_1.splitwallet.adapter

import android.graphics.Color
import android.net.TrafficStats
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.splitwallet.R.*
import edu.bluejack20_1.splitwallet.support_class.DateHelper
import edu.bluejack20_1.splitwallet.support_class.Transactions
import kotlinx.android.synthetic.main.item_transaction_detail.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionDetailAdapter(var transactionList: ArrayList<Transactions>, var months: ArrayList<String>): RecyclerView.Adapter<TransactionDetailAdapter.TransactionDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDetailViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(layout.item_transaction_detail, parent, false)
        return TransactionDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionDetailViewHolder, position: Int) {
        var list_date = DateHelper.splitDate(transactionList[position].transactionDate)
        var tempDate = SimpleDateFormat("yyyy-dd-MM").parse("${list_date[0]}-${list_date[2]}-${list_date[1]}")
        var realDate = DateFormat.getDateInstance(DateFormat.FULL).format(tempDate)
        var splittedDate = realDate.split(",")

        if(transactionList[position].transactionType == "Expense"){
            holder.transaction_amount.text = "- Rp. " + NumberFormat.getIntegerInstance().format(transactionList[position].transactionAmount)
            holder.transaction_amount.setTextColor(Color.RED)
        } else {
            holder.transaction_amount.text = "+ Rp. " + NumberFormat.getIntegerInstance().format(transactionList[position].transactionAmount)
            holder.transaction_amount.setTextColor(Color.parseColor("#0099ff"))
        }
        holder.transaction_note.text = transactionList.get(position).transactionNote
        holder.day.text = splittedDate[0].trim()
        holder.date.text = list_date[2]
        holder.month_year.text = "${months[list_date[1].toInt() - 1]} ${list_date[0]}"
    }

    class TransactionDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var transaction_note = itemView.transaction_note
        var transaction_amount = itemView.transaction_amount
        var day = itemView.day
        var date = itemView.date
        var month_year = itemView.month_year
    }

}