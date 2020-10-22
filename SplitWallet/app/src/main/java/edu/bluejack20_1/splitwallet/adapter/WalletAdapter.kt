package edu.bluejack20_1.splitwallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import kotlinx.android.synthetic.main.item_wallet.view.*
import java.text.NumberFormat

class WalletAdapter(val walletList: ArrayList<WalletsHelper>): RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    private lateinit var listener: OnItemClickListener

    class WalletViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        var walletName = itemView.wallet_name
        var walletTotal = itemView.wallet_total

        init {
            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if(listener != null){
                        var position = adapterPosition
                        if(position != RecyclerView.NO_POSITION)
                            listener.onItemClick(position)
                    }
                }
            })
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)
        return WalletViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        var currItem = walletList[position]
        var totalMoney = 0

        for(i in currItem.transactions){
            totalMoney += i.transactionAmount.toInt()
        }

        holder.walletName.text = currItem.walletName.toString()
        holder.walletTotal.text = "Rp. " + NumberFormat.getIntegerInstance().format(totalMoney)
    }

}