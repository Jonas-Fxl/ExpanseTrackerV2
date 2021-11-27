package dev.wiprojekt.expansetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BuchungDAO {

    @Query("SELECT * FROM buchungen WHERE uid == :uid ORDER BY datum DESC ")
    fun getAll(uid : String) : List<Buchung>

    @Query("DELETE FROM buchungen")
    suspend fun deleteAll()

    @Insert
    suspend fun insertBuchung(buchung: Buchung)

    @Insert
    suspend fun insertBuchungen(buchungen: List<Buchung>)

    @Update
    suspend fun updateBuchung(buchung: Buchung)
}