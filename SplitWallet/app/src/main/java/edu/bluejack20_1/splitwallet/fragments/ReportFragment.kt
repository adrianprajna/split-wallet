package edu.bluejack20_1.splitwallet.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import edu.bluejack20_1.splitwallet.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.DateHelper
import edu.bluejack20_1.splitwallet.support_class.Transactions
import edu.bluejack20_1.splitwallet.support_class.json_class.WalletGrowth
import fr.ganfra.materialspinner.MaterialSpinner
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var inf : View
    lateinit var mIncomePieChart: PieChart
    lateinit var mExpensePieChart: PieChart

    lateinit var expenseList : ArrayList<WalletGrowth>
    lateinit var incomeList : ArrayList<WalletGrowth>
    var reff_report = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USER).child(Constants.KEY_USER_ID).child(Constants.LIST_WALLET)

    lateinit var spinner : MaterialSpinner
    var listSpinner = arrayListOf<String>()
    lateinit var adapter : ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentManager?.popBackStack()
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_report, container, false)

        mIncomePieChart = inf.findViewById(R.id.report_income_pie_chart)
        mExpensePieChart = inf.findViewById(R.id.report_expense_pie_chart)
        initItems()
        getListOfData()
        spinner = inf.findViewById(R.id.report_spinner) as MaterialSpinner
        adapter = ArrayAdapter<String>(this@ReportFragment.requireContext(), android.R.layout.simple_spinner_dropdown_item, listSpinner)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != -1){
                    var selected = spinner.getItemAtPosition(p2).toString()
                    if (selected == "All"){
                        if (incomeList.isNotEmpty() && expenseList.isNotEmpty()){
                            incomeChart()
                            expenseChart()
                        }
                    } else if (selected == "Income"){
                        if (incomeList.isNotEmpty()){
                            incomeChart()
                            mExpensePieChart.visibility = View.GONE
                        }
                    } else if (selected == "Expense"){
                        if (expenseList.isNotEmpty()){
                            expenseChart()
                            mIncomePieChart.visibility = View.GONE
                        }
                    }
                } else {
                    mIncomePieChart.visibility = View.GONE
                    mExpensePieChart.visibility = View.GONE
                }
            }


        }

        return inf
    }

    fun initItems(){
        listSpinner.add("All")
        listSpinner.add("Income")
        listSpinner.add("Expense")
    }

    fun getListOfData(){
        reff_report.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                expenseList = ArrayList()
                incomeList = ArrayList()
                for (p in snapshot.children){
                    if (p.child("walletType").getValue().toString() == "Expense"){
                        expenseList.add(WalletGrowth(p.child("walletName").getValue().toString()))
                        var temp = p.child("transactions")
                        for (t in temp.children){
                            if (DateHelper.getDifferenceToNow(t.child("transactionDate").value.toString()) < 32){
                                var totalNow : Int = expenseList.get(expenseList.size - 1).walletSpent.toInt()
                                totalNow += t.child("transactionAmount").getValue().toString().toInt()
                                expenseList.get(expenseList.size - 1).walletSpent = totalNow
                            }
                        }

                    } else {
                        incomeList.add(WalletGrowth(p.child("walletName").getValue().toString()))
                        var temp = p.child("transactions")
                        for (t in temp.children){
                            if (DateHelper.getDifferenceToNow(t.child("transactionDate").value.toString()) < 32) {
                                var totalNow: Int =
                                    incomeList.get(incomeList.size - 1).walletSpent.toInt()
                                totalNow += t.child("transactionAmount").getValue().toString()
                                    .toInt()
                                incomeList.get(incomeList.size - 1).walletSpent = totalNow
                            }
                        }

                    }
                }
            }

        })
    }

    fun incomeChart(){
        var incomeBarEntries = arrayListOf<PieEntry>()
        for (p in incomeList){
            incomeBarEntries.add(PieEntry(p.walletSpent.toFloat(), p.walletName))
        }
        mIncomePieChart.visibility = View.VISIBLE
        mIncomePieChart.animateXY(5000,5000)

        var incomePieDataSet = PieDataSet(incomeBarEntries, "(Incomes in this month)")
        incomePieDataSet.colors = randomColor()
        incomePieDataSet.valueTextSize = 15.0f

        var incomePieData = PieData(incomePieDataSet)
        mIncomePieChart.data = incomePieData

        var description = Description()
        description.text = "Incomes"
        description.textSize = 20.0f
        mIncomePieChart.description = description
        mIncomePieChart.invalidate()

    }

    fun expenseChart(){


        var expenseBarEntries = arrayListOf<PieEntry>()

        for (p in expenseList){
            expenseBarEntries.add(PieEntry(p.walletSpent.toFloat(), p.walletName))
        }
        mExpensePieChart.visibility = View.VISIBLE
        mExpensePieChart.animateXY(5000,5000)
        var expensePieDataSet = PieDataSet(expenseBarEntries, "(Expenses in this month)")
        expensePieDataSet.colors = randomColor()
        expensePieDataSet.valueTextSize = 15.0f

        var expensePieData = PieData(expensePieDataSet)
        mExpensePieChart.data = expensePieData

        var descriptions = Description()
        descriptions.text = "Expenses"
        descriptions.textSize = 20.0f
        mExpensePieChart.description = descriptions
        mExpensePieChart.invalidate()
    }

    fun randomColor(): List<Int> {
        var a = Random.nextInt(5)
        var x : List<Int>
        when (a) {
            0 -> {
                x = ColorTemplate.COLORFUL_COLORS.toList()
            }
            1 -> {
                x = ColorTemplate.JOYFUL_COLORS.toList()
            }
            2 -> {
                x = ColorTemplate.LIBERTY_COLORS.toList()
            }
            3 -> {
                x = ColorTemplate.PASTEL_COLORS.toList()
            }
            4 -> {
                x = ColorTemplate.VORDIPLOM_COLORS.toList()
            }
            else -> {
                x = ColorTemplate.MATERIAL_COLORS.toList()
            }
        }
        Collections.shuffle(x)
        return x

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

