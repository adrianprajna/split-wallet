package edu.bluejack20_1.splitwallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPagerAdapter()
        setBottomNav()
        setViewPagerListener()
    }

    var exit = false

    private fun setViewPagerListener(){
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                bottom_nav.selectedItemId = when(position){
                    0 -> R.id.nav_home
                    1 -> R.id.nav_calendar
                    2 -> R.id.nav_transaction
                    3 -> R.id.nav_report
                    4 -> R.id.nav_wallet
                    else -> R.id.nav_home
                }
            }

        })
    }

    private fun setBottomNav(){
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            viewPager.currentItem = when(item.itemId) {
                R.id.nav_home -> 0
                R.id.nav_calendar -> 1
                R.id.nav_transaction -> 2
                R.id.nav_report -> 3
                R.id.nav_wallet -> 4
                else -> 0
            }

            true
        }
    }

    private fun setViewPagerAdapter(){
        viewPager.adapter =
            PageAdapter(supportFragmentManager)
    }

    @Override
    override fun onBackPressed() {
        if (exit){
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed(Runnable { exit = false }, 3 * 1000)
        }
    }


}