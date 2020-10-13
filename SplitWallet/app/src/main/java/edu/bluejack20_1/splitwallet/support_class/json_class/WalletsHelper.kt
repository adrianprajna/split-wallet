package edu.bluejack20_1.splitwallet.support_class.json_class

import android.os.Parcel
import android.os.Parcelable
import edu.bluejack20_1.splitwallet.support_class.Transactions

class WalletsHelper() : Parcelable {
    var walletName : String? = ""
    var walletType : String? = ""
    var walletLimit : Number? = 0
    var listTransactions = arrayListOf<Transactions>()

    constructor(parcel: Parcel) : this() {
        walletName = parcel.readString()
        walletType = parcel.readString()
        walletLimit = parcel.readInt()
    }


    constructor(walletName: String, walletType: String, walletLimit: Number, listTransactions: ArrayList<Transactions>?) : this() {
        this.walletName = walletName
        this.walletType = walletType
        this.walletLimit = walletLimit
        if (listTransactions != null) {
            this.listTransactions = listTransactions
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(walletName)
        dest?.writeString(walletType)
        dest?.writeInt(walletLimit!!.toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalletsHelper> {
        override fun createFromParcel(parcel: Parcel): WalletsHelper {
            return WalletsHelper(parcel)
        }

        override fun newArray(size: Int): Array<WalletsHelper?> {
            return arrayOfNulls(size)
        }
    }

}