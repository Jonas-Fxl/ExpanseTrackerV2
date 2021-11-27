package dev.wiprojekt.expansetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable
import java.text.DateFormat

@Entity(tableName = "buchungen")
data class Buchung(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="rowid") val buchungId: Int = 0,
    @ColumnInfo(name ="bezeichnung") val bezeichnung: String,
    @ColumnInfo(name ="art") val art: String,
    @ColumnInfo(name ="datum") val datum: String,
    @ColumnInfo(name ="summe") val summe: Double,
    @ColumnInfo(name ="info") val beleg: String?,
    @ColumnInfo(name = "uid") val userID : String = FirebaseAuth.getInstance().currentUser!!.uid,
    @ColumnInfo(name = "hinzugefuegt") val hinzugefuegt : Long = System.currentTimeMillis() // wird autofilled
): Serializable {
    val createdAtDateFormat: String
        get() = DateFormat.getDateTimeInstance()
            .format(hinzugefuegt) // Date Format: Jan 11, 2021, 11:30 AM
}
