package dev.wiprojekt.expansetracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.wiprojekt.expansetracker.Buchung.NeueAusgabe
import dev.wiprojekt.expansetracker.Kamera
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.databinding.FragmentMonthBinding

private lateinit var binding: FragmentMonthBinding

class MonthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_month, container, false)

        binding = FragmentMonthBinding.bind(view)
        binding.btn.setOnClickListener {
            activity?.let {
                val intent = Intent(it, Kamera::class.java)
                it.startActivity(intent)
            }
        }

        return view
    }
}