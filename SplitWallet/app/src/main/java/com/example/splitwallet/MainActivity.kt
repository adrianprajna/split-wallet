package com.example.splitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.splitwallet.adapter.PageAdapter
import com.example.splitwallet.fragments.AccountFragment
import com.example.splitwallet.fragments.CalendarFragment
import com.example.splitwallet.fragments.HomeFragment
import com.example.splitwallet.fragments.Transaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPagerAdapter()
        setBottomNav()
        setViewPagerListener()
    }

    private fun setViewPagerListener(){
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                bottom_nav.selectedItemId = when(position){
                    0 -> R.id.nav_home
                    1 -> R.id.nav_transaction
                    2 -> R.id.nav_calendar
                    3 -> R.id.nav_account
                    else -> R.id.nav_home
                }
            }

        })
    }

    private fun setBottomNav(){
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            viewPager.currentItem = when(item.itemId) {
                R.id.nav_home -> 0
                R.id.nav_transaction -> 1
                R.id.nav_calendar -> 2
                R.id.nav_account -> 3
                else -> 0
            }

            true
        }
    }

    private fun setViewPagerAdapter(){
        viewPager.adapter = PageAdapter(supportFragmentManager)
    }
}