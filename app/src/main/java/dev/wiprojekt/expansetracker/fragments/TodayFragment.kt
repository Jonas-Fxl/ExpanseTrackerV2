package dev.wiprojekt.expansetracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dev.wiprojekt.expansetracker.Buchung.NeueAusgabe
import dev.wiprojekt.expansetracker.Buchung.NeueBuchung
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.BuchungREPO
import dev.wiprojekt.expansetracker.main.MainRecyclerAdapter
import dev.wiprojekt.expansetracker.main.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repo: BuchungREPO
    private lateinit var mViewModel: MainViewModel
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.todayRecycleView)
        repo = activity?.let { BuchungREPO(it.application) }!!

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        val budget = view.findViewById<TextView>(R.id.todayBudget)
        val einnahme = view.findViewById<TextView>(R.id.today_tv_Einnahmen)
        val ausgabe = view.findViewById<TextView>(R.id.today_tv_Ausgaben)
        val ausgabe_hinzufuegen = view.findViewById<LinearLayout>(R.id.todayAusgabe)
        val einnahme_hinzufuegen = view.findViewById<LinearLayout>(R.id.todayEinnahme)

        einnahme_hinzufuegen.setOnClickListener {
            activity?.let {
                val intent = Intent(it, NeueBuchung::class.java)
                it.startActivity(intent)
            }
        }

        ausgabe_hinzufuegen.setOnClickListener {
            activity?.let {
                val intent = Intent(it, NeueAusgabe::class.java)
                it.startActivity(intent)
            }
        }


        //Momentanes Datum im Format dd.MM.yyyy
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = convertDateToLong(current.format(formatter))

        val data = mViewModel.getAllBuchungenHeute(formatted)
        for (buchung in data) {
            val adapter = MainRecyclerAdapter(requireContext(), data)
            recyclerView.adapter = adapter
        }

        val einnahmeVal = mViewModel.getAllIncomeHeute(formatted ,uid)
        val ausgabeVal = mViewModel.getAllExpenseHeute(formatted, uid)

        einnahme.text = "%.2f €".format(einnahmeVal).replace(".", ",")
        ausgabe.text = "%.2f €".format(ausgabeVal).replace(".", ",")
        budget.text = "%.2f €".format(einnahmeVal + ausgabeVal).replace(".", ",")

    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date).time
    }
}