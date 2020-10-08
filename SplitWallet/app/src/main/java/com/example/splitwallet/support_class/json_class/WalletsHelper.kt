package com.example.splitwallet.support_class.json_class

import android.icu.number.IntegerWidth
import com.example.splitwallet.support_class.Transactions

class WalletsHelper {
    var walletName : String? = ""
    var walletType : String? = ""
    var walletLimit : Number? = 0
    var listTransactions = arrayListOf<Transactions>()

    constructor()

    constructor(walletName: String, walletType: String, walletLimit: Number, listTransactions: ArrayList<Transactions>?){
        this.walletName = walletName
        this.walletType = walletType
        this.walletLimit = walletLimit
        if (listTransactions != null) {
            this.listTransactions = listTransactions
        }
    }

}