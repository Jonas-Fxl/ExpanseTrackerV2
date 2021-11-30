package dev.wiprojekt.expansetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BuchungDAO {

    @Query("SELECT * FROM buchungen WHERE uid == :uid ORDER BY datum DESC ")
    fun getAll(uid: String): List<Buchung>

    @Query("DELETE FROM buchungen WHERE rowid == :bid")
    suspend fun deleteBuchung(bid: Int)

    @Insert
    suspend fun insertBuchung(buchung: Buchung)

    @Update
    suspend fun updateBuchung(buchung: Buchung)

    @Query("SELECT SUM(summe) FROM buchungen WHERE summe < 0 AND uid == :uid")
    fun getAllExpenses(uid: String): Double

    @Query("SELECT SUM(summe) FROM buchungen WHERE summe > 0 AND uid == :uid")
    fun getAllIncome(uid: String): Double


    //Heute
    @Query("SELECT * FROM buchungen WHERE datum == :date AND uid == :uid")
    fun getAllBuchungenHeute(date: Long, uid: String): List<Buchung>

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum == :date AND  summe > 0 AND uid == :uid")
    fun getAllIncomeHeute(date: Long, uid: String): Double

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum == :date AND  summe < 0 AND uid == :uid")
    fun getAllExpenseHeute(date: Long, uid: String): Double

    //Monat
    @Query("SELECT * FROM buchungen WHERE datum > :startDatum AND datum < :endDatum AND uid == :uid" )
    fun getAllBuchungenMonat(startDatum: Long, endDatum: Long, uid: String): List<Buchung> // enddatum mussd er 01. des nächsten Monats sein!

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum BETWEEN :startDatum AND :endDatum AND datum != :endDatum AND uid == :uid" )
    fun getAllIncomeMonth(startDatum: Long, endDatum: Long, uid: String): Double

    @Query("SELECT SUM(summe) FROM buchungen WHERE datum BETWEEN :startDatum AND :endDatum AND datum != :endDatum AND uid == :uid")
    fun getAllExpenseMonth(startDatum: Long, endDatum: Long, uid: String): Double
}