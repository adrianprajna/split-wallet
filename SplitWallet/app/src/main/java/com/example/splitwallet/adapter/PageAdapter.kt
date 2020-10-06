package com.example.splitwallet.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.splitwallet.fragments.AccountFragment
import com.example.splitwallet.fragments.CalendarFragment
import com.example.splitwallet.fragments.HomeFragment
import com.example.splitwallet.fragments.Transaction

class PageAdapter(var fm: FragmentManager): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> Transaction()
            2 -> CalendarFragment()
            3 -> AccountFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }


}