package dev.wiprojekt.expansetracker.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dev.wiprojekt.expansetracker.Buchung.NeueAusgabe
import dev.wiprojekt.expansetracker.Buchung.NeueBuchung
import dev.wiprojekt.expansetracker.LOG_TAG
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.data.BuchungREPO
import dev.wiprojekt.expansetracker.main.MainRecyclerAdapter
import dev.wiprojekt.expansetracker.main.MainViewModel
import dev.wiprojekt.expansetracker.main.SharedViewModel
import dev.wiprojekt.expansetracker.preferences.PrefHelper
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment(),
    MainRecyclerAdapter.BuchungItemListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repo: BuchungREPO
    private lateinit var mViewModel: MainViewModel
    private lateinit var uid: String
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main2, container, false)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.todayRecycleView)
        repo = activity?.let { BuchungREPO(it.application) }!!

        return view }

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
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val startDatum = "01.01.${year}"
        val endDatum = "01.01.${year + 1}"

        val data = mViewModel.getAllBuchungenMonat(convertDateToLong(startDatum), convertDateToLong(endDatum), uid).sortedBy { it.datum }
        for (buchung in data) {
            val adapter = MainRecyclerAdapter(requireContext(), data, this)
            recyclerView.adapter = adapter
        }

        val einnahmeVal = mViewModel.getAllIncomeMonth(convertDateToLong(startDatum), convertDateToLong(endDatum), uid) //ändern
        val ausgabeVal = mViewModel.getAllExpenseMonth(convertDateToLong(startDatum), convertDateToLong(endDatum), uid) //ändern

        val pref = PrefHelper
        pref.loadSettings(requireContext())
        val jahresBudget = pref.budget.toDouble() * 12
        einnahme.text = "%.2f ${PrefHelper.currency}".format(einnahmeVal)
        ausgabe.text = "%.2f ${PrefHelper.currency}".format(ausgabeVal)
        budget.text = "%.2f ${PrefHelper.currency}".format(jahresBudget + einnahmeVal + ausgabeVal)

    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date)!!.time
    }

    override fun onBuchungItemClick(buchung: Buchung) {
        val newViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        newViewModel.selectetBuchung.value = buchung
        newViewModel.herkunft = "Month"
        navController = Navigation.findNavController(
            requireActivity(), R.id.fr_wrapper
        )
        navController.navigate(R.id.action_monthFragment_to_detailFragment)
    }
}