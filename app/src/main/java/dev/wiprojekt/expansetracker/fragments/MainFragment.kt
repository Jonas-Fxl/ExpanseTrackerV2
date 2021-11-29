package dev.wiprojekt.expansetracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import dev.wiprojekt.expansetracker.preferences.PrefHelper


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
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.recycleView)
        repo = activity?.let { BuchungREPO(it.application) }!!
        navController = Navigation.findNavController(
            requireActivity(), R.id.fr_wrapper
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        val budget = view.findViewById<TextView>(R.id.budget)
        val einnahme = view.findViewById<TextView>(R.id.einnahmen)
        val ausgabe = view.findViewById<TextView>(R.id.ausgaben)
        val ausgabe_hinzufuegen = view.findViewById<LinearLayout>(R.id.ausgabe)
        val einnahme_hinzufuegen = view.findViewById<LinearLayout>(R.id.einnahme)

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
        mViewModel.buchungData.observe(viewLifecycleOwner, Observer
        {
            val einnahmeVal = mViewModel.getAllIncome(uid)
            val ausgabeVal = mViewModel.getAllExpense(uid)
            for (buchung in it) {
                val adapter = MainRecyclerAdapter(requireContext(), it, this)
                recyclerView.adapter = adapter
                Log.i(
                    LOG_TAG,
                    "${buchung.bezeichnung} (\$${buchung.summe}) at ${buchung.createdAtDateFormat} from \$${buchung.userID}"
                )
            }
            val pref = PrefHelper
            pref.loadSettings(requireContext())
            val budget_value = PrefHelper.budget.toDouble()*12
            einnahme.text = "%.2f ${PrefHelper.currency}".format(einnahmeVal)
            ausgabe.text = "%.2f ${PrefHelper.currency}".format(ausgabeVal)
            budget.text =
                "%.2f ${PrefHelper.currency}".format((einnahmeVal + budget_value) + ausgabeVal)
        })
    }

    override fun onBuchungItemClick(buchung: Buchung) {
        Log.i(LOG_TAG, "Ausgewählte Buchung: ${buchung.bezeichnung}")
        navController.navigate(R.id.action_main_to_detailFragment)
    }
}