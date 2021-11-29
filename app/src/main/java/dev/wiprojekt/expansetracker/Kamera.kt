package dev.wiprojekt.expansetracker

import android.os.Bundle
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
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.databinding.ActivityKameraBinding
import dev.wiprojekt.expansetracker.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityKameraBinding
private lateinit var viewModel: MainViewModel

class Kamera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityKameraBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


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

    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    val buchungspeichern = findViewById<Button>(R.id.buchungspeichern)
    val schliessen = findViewById<ImageButton>(R.id.schliessen)


    buchungspeichern.setOnClickListener {
        insertDataToDatabase()
        finish()
    }

    schliessen.setOnClickListener {
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
    val summe = summeText.text.toString().toDouble() * - 1
    val datum = datumText.text.toString()
    val art = artText.text.toString()
    val info = beschreibungText.text.toString()

    val bitmap = binding.fotoPreview.drawable.toBitmap()

    if (inputCheck(bezeichnung, summe, datum, art, info)) {

        val buchung = Buchung(0, bezeichnung, art, convertDateToLong(datum), summe, info, bitmap)
        //Enter in Viewmodel
        viewModel.insertBuchung(buchung)
        Toast.makeText(this, "Erfolgreich hinzugefügt!", Toast.LENGTH_LONG).show()
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
}