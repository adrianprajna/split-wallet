package edu.bluejack20_1.splitwallet.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.Wallets
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletsHelper
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletAdapter(val walletList: ArrayList<WalletsHelper>): RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    inner class WalletViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var walletName = itemView.wallet_name
        var walletTotal = itemView.wallet_total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)

        return WalletViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        var currItem = walletList[position]
        var totalMoney = 0

        for(i in currItem.listTransactions){
            totalMoney += i.transactionAmount.toInt()
        }

        holder.walletName.text = currItem.walletName.toString()
        holder.walletTotal.text = "Rp. ${totalMoney}"
    }

}