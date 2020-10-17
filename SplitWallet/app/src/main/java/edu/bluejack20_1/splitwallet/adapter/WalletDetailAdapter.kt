package edu.bluejack20_1.splitwallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlinx.android.synthetic.main.item_wallet.view.wallet_name
import kotlinx.android.synthetic.main.item_wallet_detail.view.*

class WalletDetailAdapter(var walletList: ArrayList<Wallets>): RecyclerView.Adapter<WalletDetailAdapter.WalletDetailViewHolder>(){

    private lateinit var listener: OnItemClickListener

    class WalletDetailViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        var walletName = itemView.wallet_name
        var walletType = itemView.wallet_type

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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletDetailViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_detail, parent, false)
        return WalletDetailViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(holder: WalletDetailViewHolder, position: Int) {
        holder.walletName.text = walletList.get(position).walletName
        holder.walletType.text = walletList.get(position).walletType
    }


}