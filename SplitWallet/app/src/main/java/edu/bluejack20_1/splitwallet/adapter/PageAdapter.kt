package edu.bluejack20_1.splitwallet.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import edu.bluejack20_1.splitwallet.fragments.*

class PageAdapter(var fm: FragmentManager): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> Transaction()
            2 -> ReportFragment()
            3 -> WalletFragment()
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }


}