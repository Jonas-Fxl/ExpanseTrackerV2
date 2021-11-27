package dev.wiprojekt.expansetracker.data

import android.app.Application
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuchungREPO(val application: Application) {

    val buchungData = MutableLiveData<List<Buchung>>()
    private val dao = BuchungDatenbank.getDatabase(application).buchungDao()
    val data = dao.getAll(FirebaseAuth.getInstance().currentUser!!.uid)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val getAll = dao.getAll(FirebaseAuth.getInstance().currentUser!!.uid)
            if (getAll.isEmpty()){
                withContext(Dispatchers.Main){
                    Toast.makeText(application, "Room Database mit ${getAll.size}", Toast.LENGTH_LONG).show() }
            }else{
                buchungData.postValue(getAll)
                withContext(Dispatchers.Main){
                    Toast.makeText(application, "Room Database mit ${getAll.size}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    suspend fun insertBuchung(buchung: Buchung){
        dao.insertBuchung(buchung)
    }

    private fun networkSync(){ // -> Wenn offline Datenbank leer, Daten aus Firebase ziehen
        TODO()
    }
}