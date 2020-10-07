package com.example.splitwallet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.splitwallet.R
import com.example.splitwallet.support_class.Constants
import com.example.splitwallet.support_class.PreferenceConfig
import com.example.splitwallet.support_class.Users
import com.example.splitwallet.support_class.Wallets
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var inf = inflater.inflate(R.layout.fragment_home, container, false)

        if (activity != null){
            var username_home = inf.findViewById<TextView>(R.id.home_username)
            var email_home = inf.findViewById<TextView>(R.id.home_email)
            var preferenceConfig : PreferenceConfig
            preferenceConfig = PreferenceConfig(requireActivity().applicationContext)


            val u : Users
            u = preferenceConfig.getGson().fromJson(preferenceConfig.getString(Constants.KEY_USER), Users::class.java)
            username_home.setText(u.username)
            email_home.setText(u.email)

            addWallet(u)
        }
        return inf
    }

    fun addWallet(u : Users){
        var reff = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER).child(u.email!!.split("@gmail.com")[0])

        var list = arrayListOf<Wallets>()
        list.add(Wallets("Food",
            "Expense",
            300000))
        list.add(Wallets("Salary",
            "Income",
            0))
        reff.child("listWallets").setValue(list)
    }





}