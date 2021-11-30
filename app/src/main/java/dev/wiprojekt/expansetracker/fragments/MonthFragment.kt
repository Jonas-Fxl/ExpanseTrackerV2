package dev.wiprojekt.expansetracker.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.wiprojekt.expansetracker.R
import java.util.*
import java.util.Calendar.DAY_OF_MONTH

class MonthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_month, container, false)

        val cal = Calendar.getInstance()
        val tv = view.findViewById<TextView>(R.id.textView)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var m = ""
        var d = ""
        val day = cal.get(DAY_OF_MONTH)

        view.findViewById<Button>(R.id.btn).setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    m = if (month + 1 < 10) {
                        "0" + (month + 1).toString()
                    } else {
                        (month + 1).toString()
                    }
                    d = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        dayOfMonth.toString()
                    }
                    tv?.text = "${d}.${m}.${year}"
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        return view
    }


}