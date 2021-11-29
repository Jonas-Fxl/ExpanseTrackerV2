package dev.wiprojekt.expansetracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dev.wiprojekt.expansetracker.data.Buchung
import dev.wiprojekt.expansetracker.data.BuchungREPO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app : Application) : AndroidViewModel(app){

    private val dataRepo = BuchungREPO(app)
    val buchungData = dataRepo.buchungData

    fun insertBuchung(buchung: Buchung){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepo.insertBuchung(buchung)
        }
    }

    suspend fun updateBuchung(buchung: Buchung){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepo.updateBuchung(buchung)
        }
    }

    fun getAllExpense(uid: String): Double {
        return dataRepo.getAllExpense(uid)
    }

    fun getAllIncome(uid: String): Double {
            return dataRepo.getAllIncome(uid)
    }

    fun getAllBuchungenHeute(date: Long): List<Buchung> {
        return dataRepo.getAllBuchungenHeute(date)
    }

    fun getAllIncomeHeute(date : Long, uid: String): Double{
        return dataRepo.getAllIncomeHeute(date, uid)
    }

    fun getAllExpenseHeute(date : Long, uid: String): Double{
        return dataRepo.getAllExpenseHeute(date, uid)
    }

}