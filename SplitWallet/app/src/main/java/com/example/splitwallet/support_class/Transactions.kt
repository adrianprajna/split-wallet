package com.example.splitwallet.support_class

import java.util.*

class Transactions {
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



