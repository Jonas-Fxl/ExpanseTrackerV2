package dev.wiprojekt.expansetracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.main.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var mViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        mViewModel.selectetBuchung.observe(viewLifecycleOwner, Observer {
            Log.i(LOG_TAG, "Selected: ${it.bezeichnung}")
        })

        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val headerText = view.findViewById<TextView>(R.id.detail_neue_ausgabe)
        val bezeichnungText = view.findViewById<TextView>(R.id.bezeichnungText)
        val summeText = view.findViewById<TextView>(R.id.summeText)
        val datumText = view.findViewById<TextView>(R.id.datumText)
        val artText = view.findViewById<TextView>(R.id.artText)
        val beschreibungText = view.findViewById<TextView>(R.id.beschreibungText)
        val hinzugefuegtText = view.findViewById<TextView>(R.id.detailHinzugefuegt)
        val buchungID = view.findViewById<TextView>(R.id.detailBuchungsnummer)

        headerText.text = mViewModel.selectetBuchung.value?.bezeichnung
        bezeichnungText.text = headerText.text
        summeText.text = mViewModel.selectetBuchung.value?.summe.toString()
        datumText.text = convertLongToTime(mViewModel.selectetBuchung.value!!.datum)
        artText.text = mViewModel.selectetBuchung.value?.art
        beschreibungText.text = mViewModel.selectetBuchung.value?.beleg
        hinzugefuegtText.text =
            "Hinzugefügt am: " + convertLongToTime(mViewModel.selectetBuchung.value!!.hinzugefuegt)
        buchungID.text = "Buchungs-ID: " + mViewModel.selectetBuchung.value?.buchungId.toString()

        view.findViewById<ImageView>(R.id.fotoPreview).setImageBitmap(mViewModel.selectetBuchung.value?.datei)


        val update = view.findViewById<Button>(R.id.buchungspeichern)
        update.setOnClickListener {
            updateBuchung(
                buchungID,
                beschreibungText,
                artText,
                datumText,
                summeText,
                bezeichnungText,
                view.findViewById<ImageView>(R.id.fotoPreview).drawable.toBitmap()
            )

            navigateBack()
        }

        val loeschen = view.findViewById<Button>(R.id.loeschen)
        loeschen.setOnClickListener {
            loeschen(mViewModel.selectetBuchung.value!!.buchungId)
            navigateBack()
        }

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {

            view.findViewById<ImageView>(R.id.fotoPreview).setImageURI(it)
            Log.i(LOG_TAG, "Hinzugefuegt: " + it.toString())

        }


        view.findViewById<Button>(R.id.takePhoto).setOnClickListener {

            getImage.launch("image/*")

        }

        return view
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date)!!.time
    }

    private fun updateBuchung(
        buchungID: TextView,
        beschreibungText: TextView,
        artText: TextView,
        datumText: TextView,
        summeText: TextView,
        bezeichnungText: TextView,
        bitmap: Bitmap
    ) {

        val bezeichnung = bezeichnungText.text.toString()
        val summe = summeText.text.toString().toDouble()
        val datum = datumText.text.toString()
        val art = artText.text.toString()
        val info = beschreibungText.text.toString()
        val id = mViewModel.selectetBuchung.value!!.buchungId

        if (inputCheck(bezeichnung, summe, datum, art, info)) {

            val buchung = Buchung(id, bezeichnung, art, convertDateToLong(datum), summe, info, bitmap)
            //Enter in Viewmodel
            mViewModel.updateBuchung(buchung)
            Toast.makeText(requireContext(), "Erfolgreich hinzugefügt!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(
        bezeichnung: String,
        summe: Double?,
        datum: String,
        art: String,
        info: String
    ): Boolean {
        if (bezeichnung.isEmpty()) {
            val bezeichung_layout = view?.findViewById<TextInputLayout>(R.id.bezeichung_layout)
            bezeichung_layout?.error = "Bitte eine Bezeichnung eingeben."
            return false
        }
        if (art.isEmpty()) {
            val art_layout = view?.findViewById<TextInputLayout>(R.id.art_layout)
            art_layout?.error = "Bitte definieren: Budget, Einnahme, Ausgabe."
            return false
        }

        if (datum.isEmpty()) {
            val datum_layout = view?.findViewById<TextInputLayout>(R.id.datum_layout)
            datum_layout?.error = "Geben Sie ein gültiges Datum ein."
            return false
        }
        if (summe == null) {
            val summe_layout = view?.findViewById<TextInputLayout>(R.id.summe_layout)
            summe_layout?.error = "Bitte Betrag eingeben."
            return false
        }
        return true
    }

    private fun navigateBack() {
        val navController = Navigation.findNavController(
            requireActivity(), R.id.fr_wrapper
        )
        if (mViewModel.herkunft == "Main") {
            navController.navigate(R.id.action_detailFragment_to_mainFragment)
        } else if (mViewModel.herkunft == "Today") {
            navController.navigate(R.id.action_detailFragment_to_todayFragment)
        }
    }

    private fun loeschen(bid: Int){
        mViewModel.deleteBuchung(bid)
        Toast.makeText(requireContext(), "Buchung wurde gelöscht", Toast.LENGTH_LONG).show()
    }
}