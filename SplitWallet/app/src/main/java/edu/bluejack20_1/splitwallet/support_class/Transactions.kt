package edu.bluejack20_1.splitwallet.support_class

import java.util.*

class Transactions {
    var transactionDate : Date = Date()
    var transactionNote : String = ""
    var transactionAmount : Number = 0
    var transactionType : String = ""

    constructor()

    constructor(transactionDate : Date, transactionNote : String, transactionAmount : Number, transactionType : String){
        this.transactionDate = transactionDate
        this.transactionNote = transactionNote
        this.transactionAmount = transactionAmount
        this.transactionType = transactionType
    }

}



