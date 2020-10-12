package edu.bluejack20_1.splitwallet.support_class

class Users {
    var username: String? = ""
    var password: String? = ""
    var email: String? = ""
    var notification: String = "true"
//    var listWallets : List<Wallets>? = null

    constructor(){}
    constructor(username: String?, password:String?, email:String?){
        this.username = username
        this.password = password
        this.email = email
    }

}
