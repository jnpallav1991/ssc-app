package com.sales.ssc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = arrayOf(CartDetails::class),
    version = 1
)
@TypeConverters(DateConverter::class)
public abstract class SscDatabase : RoomDatabase() {

    abstract fun cartDetailsDao(): CartDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: SscDatabase? = null

        fun getDatabase(context: Context): SscDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SscDatabase::class.java,
                    "ssc_database"
                ).setJournalMode(JournalMode.TRUNCATE)
                    .addMigrations()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}