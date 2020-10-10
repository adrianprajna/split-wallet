package edu.bluejack20_1.splitwallet.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import edu.bluejack20_1.splitwallet.MainActivity
import edu.bluejack20_1.splitwallet.R
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.text.DateFormat
import java.util.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Transaction.newInstance] factory method to
 * create an instance of this fragment.
 */
class Transaction : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var inf: View
    lateinit var day: Number
    lateinit var month: Number
    lateinit var year: Number

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_transaction, container, false)

        var btn_date = inf.findViewById<Button>(R.id.btn_date)
        btn_date.setOnClickListener(){
            showDatePickerDialog()
        }

        return inf
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        var calendar = Calendar.getInstance()
//        calendar.set(Calendar.YEAR, year)
//        calendar.set(Calendar.MONTH, month)
//        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//        var currentDate = DateFormat.getDateInstance().format(calendar.time)
        this.day = dayOfMonth
        this.month = month
        this.year = year


        var months = listOf<String>("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        var btn_date = inf.findViewById<Button>(R.id.btn_date)
        btn_date.setText("${months[month]} $dayOfMonth, $year")
    }

    private fun showDatePickerDialog(){
        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        var datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

}