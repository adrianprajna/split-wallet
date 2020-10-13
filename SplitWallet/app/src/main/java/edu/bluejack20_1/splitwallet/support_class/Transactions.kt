package edu.bluejack20_1.splitwallet.support_class

import java.io.Serializable


class Transactions : Serializable{
    var transactionDate : String = ""
    var transactionNote : String = ""
    var transactionAmount : Number = 0
    var transactionType : String = ""

    constructor()

    constructor(transactionDate : String, transactionNote : String, transactionAmount : Number, transactionType : String){
        this.transactionDate = transactionDate
        this.transactionNote = transactionNote
        this.transactionAmount = transactionAmount
        this.transactionType = transactionType
    }

}



