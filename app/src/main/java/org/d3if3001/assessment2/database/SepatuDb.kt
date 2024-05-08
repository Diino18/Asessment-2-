package org.d3if3001.assessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3001.assessment2.model.Sepatu

@Database(entities = [Sepatu::class], version = 1, exportSchema = false)
abstract class SepatuDb : RoomDatabase() {
    abstract val dao: SepatuDao
    companion object {
        @Volatile
        private var INSTACE: SepatuDb? = null
        fun getInstance(context: Context): SepatuDb{
            synchronized(this){
                var instance = INSTACE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SepatuDb::class.java,
                        "sepatu.db"
                    ).build()
                    INSTACE = instance
                }
                return instance
            }
        }
    }
}