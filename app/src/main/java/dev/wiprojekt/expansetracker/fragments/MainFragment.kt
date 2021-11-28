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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dev.wiprojekt.expansetracker.Buchung.NeueAusgabe
import dev.wiprojekt.expansetracker.Buchung.NeueBuchung
import dev.wiprojekt.expansetracker.LOG_TAG
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.BuchungREPO
import dev.wiprojekt.expansetracker.main.MainRecyclerAdapter
import dev.wiprojekt.expansetracker.main.MainViewModel


class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repo: BuchungREPO
    private lateinit var mViewModel: MainViewModel
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.recycleView)
        repo = activity?.let { BuchungREPO(it.application) }!!

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
                val adapter = MainRecyclerAdapter(requireContext(), it)
                recyclerView.adapter = adapter
                Log.i(
                    LOG_TAG,
                    "${buchung.bezeichnung} (\$${buchung.summe}) at ${buchung.createdAtDateFormat} from \$${buchung.userID}"
                )
            }
            einnahme.text = "%.2f €".format(einnahmeVal).replace(".", ",")
            ausgabe.text = "%.2f €".format(ausgabeVal).replace(".", ",")
            budget.text = "%.2f €".format(einnahmeVal + ausgabeVal).replace(".", ",")
        })
    }
}