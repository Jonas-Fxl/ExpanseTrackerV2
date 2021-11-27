package dev.wiprojekt.expansetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Buchung::class], version = 1, exportSchema = false)
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