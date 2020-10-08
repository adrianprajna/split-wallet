package edu.bluejack20_1.splitwallet.support_class

class Wallets {
    var walletName : String? = ""
    var walletType : String? = ""
    var walletLimit : Number? = 0
//    var listTransactions = arrayListOf<Transactions>()

    constructor()

    constructor(walletName: String, walletType: String, walletLimit: Number){
        this.walletName = walletName
        this.walletType = walletType
        this.walletLimit = walletLimit

    }

}