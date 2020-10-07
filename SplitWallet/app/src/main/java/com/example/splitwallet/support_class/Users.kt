package com.example.splitwallet.support_class

class Users {
    var username: String? = ""
    var password: String? = ""
    var email: String? = ""
    var listWallets : List<Wallets>? = null

    constructor(){}
    constructor(username: String?, password:String?, email:String?, listWallets: List<Wallets>?){
        this.username = username
        this.password = password
        this.email = email
        this.listWallets = listWallets
    }
}
