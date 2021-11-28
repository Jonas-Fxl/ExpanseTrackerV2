package dev.wiprojekt.expansetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BuchungDAO {

    @Query("SELECT * FROM buchungen WHERE uid == :uid ORDER BY datum DESC ")
    fun getAll(uid: String): List<Buchung>

    @Query("DELETE FROM buchungen")
    suspend fun deleteAll()

    @Insert
    suspend fun insertBuchung(buchung: Buchung)

    @Insert
    suspend fun insertBuchungen(buchungen: List<Buchung>)

    @Update
    suspend fun updateBuchung(buchung: Buchung)

    @Query("SELECT SUM(summe) FROM buchungen WHERE summe < 0 AND uid == :uid")
    fun getAllExpenses(uid: String): Double

    @Query("SELECT SUM(summe) FROM buchungen WHERE summe > 0 AND uid == :uid")
    fun getAllIncome(uid: String): Double

    @Query("SELECT * FROM buchungen WHERE datum == :date")
    fun getAllBuchungenHeute(date: Long): List<Buchung>

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum == :date AND  summe > 0 AND uid == :uid")
    fun getAllIncomeHeute(date: Long, uid: String): Double

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum == :date AND  summe < 0 AND uid == :uid")
    fun getAllExpenseHeute(date: Long, uid: String): Double


    @Query("SELECT * FROM buchungen WHERE datum BETWEEN :startDatum AND :endDatum" )
    fun getAllBuchungenMonat(startDatum: String, endDatum: String): List<Buchung> // enddatum mussd er 01. des nächsten Monats sein!
}