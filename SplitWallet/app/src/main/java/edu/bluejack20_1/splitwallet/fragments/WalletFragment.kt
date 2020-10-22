package edu.bluejack20_1.splitwallet.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.WalletDetailActivity
import edu.bluejack20_1.splitwallet.adapter.WalletDetailAdapter
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users
import edu.bluejack20_1.splitwallet.support_class.Wallets
import kotlin.reflect.typeOf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletFragment : Fragment() {

    private lateinit var walletList: ArrayList<Wallets>
    private lateinit var adapter: WalletDetailAdapter
    private lateinit var recyclerView: RecyclerView
    private var ref = FirebaseDatabase.getInstance().getReference(Constants.KEY_USER)
        .child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)
    private lateinit var inf: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        inf = inflater.inflate(R.layout.fragment_wallet, container, false)

        if(activity != null){
            initView(inf)
        }

        return inf
    }

    fun initView(inf : View){
        recyclerView = inf.findViewById(R.id.recycler_view)
        getAllWallets()
    }

    private fun getAllWallets(){
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                walletList = ArrayList()
                for(p in snapshot.children){
                    Log.d("key", p.child("walletLimit").getValue().toString())
                    walletList.add(Wallets(walletName = p.child("walletName").value.toString(),
                    walletLimit = p.child("walletLimit").value.toString().toInt(),
                    walletType = p.child("walletType").value.toString()))
                }

                setAdapter()
            }
        })
    }

    private fun setAdapter(){
        adapter = WalletDetailAdapter(walletList, arrayListOf<String>(getString(R.string.incomes), getString(R.string.expenses)))

        var tempLayout = inf.findViewById<RelativeLayout>(R.id.tempLayout)
        var realLayout = inf.findViewById<LinearLayout>(R.id.realLayout)
        if(walletList.isEmpty()){
            realLayout.visibility = View.GONE
            tempLayout.visibility = View.VISIBLE
        } else {
            tempLayout.visibility = View.GONE
            realLayout.visibility = View.VISIBLE
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        adapter.setOnItemClickListener(object : WalletDetailAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                var intent = Intent(context, WalletDetailActivity::class.java)
                intent.putExtra("walletName", walletList[position].walletName)
                intent.putExtra("wallet", walletList[position])
                startActivity(intent)
            }
        })
    }


}