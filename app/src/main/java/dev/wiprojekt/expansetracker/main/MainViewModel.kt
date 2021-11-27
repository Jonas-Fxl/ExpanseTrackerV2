package dev.wiprojekt.expansetracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.data.BuchungREPO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app : Application) : AndroidViewModel(app){

    private val dataRepo = BuchungREPO(app)
    val buchungData = dataRepo.buchungData
    init {

    }

    fun insertBuchung(buchung: Buchung){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepo.insertBuchung(buchung)
        }
    }

}