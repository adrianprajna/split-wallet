package com.example.splitwallet.support_class

import android.icu.number.IntegerWidth

class Wallets {
    var walletName : String? = ""
    var walletType : String? = ""
    var walletLimit : Number? = 0
    var listTransactions = arrayListOf<Transactions>()

    constructor()

    constructor(walletName: String, walletType: String, walletLimit: Number){
        this.walletName = walletName
        this.walletType = walletType
        this.walletLimit = walletLimit

    }

}