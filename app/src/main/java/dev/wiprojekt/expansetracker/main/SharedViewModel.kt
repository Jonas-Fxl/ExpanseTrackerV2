package dev.wiprojekt.expansetracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.data.BuchungREPO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    private val dataRepo = BuchungREPO(app)
    val buchungData = dataRepo.buchungData
    var selectetBuchung = MutableLiveData<Buchung>()
    var herkunft = "Main"

    fun updateBuchung(buchung: Buchung){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepo.updateBuchung(buchung)
        }
    }
}