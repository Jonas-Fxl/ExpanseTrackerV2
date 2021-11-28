package dev.wiprojekt.expansetracker.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuchungREPO(val application: Application) {

    val buchungData = MutableLiveData<List<Buchung>>()
    private val dao = BuchungDatenbank.getDatabase(application).buchungDao()
    val data = dao.getAll(FirebaseAuth.getInstance().currentUser!!.uid)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val getAll = dao.getAll(FirebaseAuth.getInstance().currentUser!!.uid)
            if (getAll.isNotEmpty()) {
                buchungData.postValue(getAll)
            }
        }
    }

    suspend fun insertBuchung(buchung: Buchung) {
        dao.insertBuchung(buchung)
    }

    fun getAllIncome(uid: String): Double {
        return dao.getAllIncome(uid)
    }

    fun getAllExpense(uid: String): Double {
        return dao.getAllExpenses(uid)
    }

    fun getAllBuchungenHeute(date: Long): List<Buchung> {
        return dao.getAllBuchungenHeute(date)
    }

    fun getAllIncomeHeute(date: Long, uid: String): Double {
        return dao.getAllIncomeHeute(date, uid)
    }

    fun getAllExpenseHeute(date: Long, uid: String): Double {
        return dao.getAllExpenseHeute(date, uid)
    }

    private fun networkSync() { // -> Wenn offline Datenbank leer, Daten aus Firebase ziehen
        TODO()
    }
}