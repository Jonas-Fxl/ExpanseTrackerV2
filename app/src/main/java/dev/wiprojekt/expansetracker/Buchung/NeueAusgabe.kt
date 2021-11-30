package dev.wiprojekt.expansetracker.Buchung

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import dev.wiprojekt.expansetracker.LOG_TAG
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.databinding.ActivityNeueAusgabeBinding
import dev.wiprojekt.expansetracker.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class NeueAusgabe : AppCompatActivity() {

    lateinit var binding: ActivityNeueAusgabeBinding

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeueAusgabeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val buchungspeichern = findViewById<Button>(R.id.buchungspeichern)


        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                binding.fotoPreview.setImageURI(it)
                Log.i(LOG_TAG, "Hinzugefuegt: " + it.toString())

            }
        )

        binding.takePhoto.setOnClickListener {

            getImage.launch("image/*")

        }

        binding.datumText.setOnClickListener {
            datumPicker()
        }

        binding.datumText.isFocusable = false
        binding.datumText.setOnFocusChangeListener { _, hasFokus ->
            if (hasFokus){
                datumPicker()
            }}

        buchungspeichern.setOnClickListener {
            insertDataToDatabase()
            finish()
        }

        binding.schliessen.setOnClickListener {
            finish()
        }
    }

    private fun insertDataToDatabase() {
        val bezeichnungText = findViewById<TextView>(R.id.bezeichnungText)
        val summeText = findViewById<TextView>(R.id.summeText)
        val datumText = findViewById<TextView>(R.id.datumText)
        val artText = findViewById<TextView>(R.id.artText)
        val beschreibungText = findViewById<TextView>(R.id.beschreibungText)

        val bezeichnung = bezeichnungText.text.toString()
        val summe = summeText.text.toString().toDouble() * -1
        val datum = datumText.text.toString()
        val art = artText.text.toString()
        val info = beschreibungText.text.toString()
        if (inputCheck(bezeichnung, summe, datum, art)) {

            val bitmap = binding.fotoPreview.drawable.toBitmap()

            val buchung =
                Buchung(0, bezeichnung, art, convertDateToLong(datum), summe, info, bitmap)
            viewModel.insertBuchung(buchung)
            Toast.makeText(this, "Erfolgreich hinzugefügt!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(
        bezeichnung: String,
        summe: Double?,
        datum: String,
        art: String
    ): Boolean {
        if (bezeichnung.isEmpty()) {
            val bezeichung_layout = findViewById<TextInputLayout>(R.id.bezeichung_layout)
            bezeichung_layout.error = "Bitte eine Bezeichnung eingeben."
            return false
        }
        if (art.isEmpty()) {
            val art_layout = findViewById<TextInputLayout>(R.id.art_layout)
            art_layout.error = "Bitte definieren: Budget, Einnahme, Ausgabe."
            return false
        }

        if (datum.isEmpty()) {
            val datum_layout = findViewById<TextInputLayout>(R.id.datum_layout)
            datum_layout.error = "Geben Sie ein gültiges Datum ein."
            return false
        }
        if (summe == null) {
            val summe_layout = findViewById<TextInputLayout>(R.id.summe_layout)
            summe_layout.error = "Bitte Betrag eingeben."
            return false
        }
        return true
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.parse(date).time
    }

    private fun datumPicker() {
        val cal = Calendar.getInstance()
        val tv = binding.datumText
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var m = ""
        var d = ""
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            this,
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
                tv.text = ("${d}.${m}.${year}").toEditable()
            },
            year,
            month,
            day
        )
        datePicker.show()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}
