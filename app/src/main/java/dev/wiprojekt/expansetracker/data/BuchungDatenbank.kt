package dev.wiprojekt.expansetracker.data

import android.content.Context
import androidx.room.*
import dev.wiprojekt.expansetracker.Buchung.BitmapKonverter

@Database(entities = [Buchung::class], version = 1, exportSchema = false)
@TypeConverters(BitmapKonverter::class)
abstract class BuchungDatenbank: RoomDatabase() {

    abstract fun buchungDao() : BuchungDAO

    companion object{
        @Volatile
        private var INSTANCE: BuchungDatenbank? = null

        fun getDatabase(context: Context): BuchungDatenbank{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BuchungDatenbank::class.java,
                    "buchungen.db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}