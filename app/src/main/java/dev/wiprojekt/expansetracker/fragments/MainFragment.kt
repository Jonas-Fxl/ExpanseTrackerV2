package dev.wiprojekt.expansetracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.wiprojekt.expansetracker.LOG_TAG
import dev.wiprojekt.expansetracker.NeueBuchung
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.data.BuchungREPO
import dev.wiprojekt.expansetracker.main.MainRecyclerAdapter
import dev.wiprojekt.expansetracker.main.MainViewModel


class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.recycleView)
        val repo = activity?.let { BuchungREPO(it.application) }
        val adapter = repo?.let { MainRecyclerAdapter(requireContext(), it.data) }
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view.findViewById<FloatingActionButton>(R.id.neu_hnzfg)
        btn.setOnClickListener {
            activity?.let {
                val intent = Intent (it, NeueBuchung::class.java)
                it.startActivity(intent)
            }
        }

        mViewModel.buchungData.observe(viewLifecycleOwner, Observer
        {
            for(buchung in it){
                Log.i(
                    LOG_TAG,
                    "${buchung.bezeichnung} (\$${buchung.summe}) at ${buchung.createdAtDateFormat} from \$${buchung.userID}")
            }
        })
    }
}