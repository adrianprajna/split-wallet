package edu.bluejack20_1.splitwallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.Transactions
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionAdapter(private var transactionList: ArrayList<Transactions>): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.transactionNote.text = transactionList[position].transactionNote
        holder.transactionAmount.text = "Rp. " + transactionList[position].transactionAmount.toString()
    }

    class TransactionViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        var transactionNote = itemView.transaction_note
        var transactionAmount = itemView.transaction_amount

        init {
            itemView.setOnClickListener(){
                if(listener != null){
                    var position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}