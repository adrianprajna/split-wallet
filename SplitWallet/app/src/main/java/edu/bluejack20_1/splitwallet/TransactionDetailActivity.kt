package edu.bluejack20_1.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig

class TransactionDetailActivity : AppCompatActivity() {

    lateinit var preferenceConfig : PreferenceConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceConfig =
            PreferenceConfig(
                applicationContext
            )
        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
    }
}