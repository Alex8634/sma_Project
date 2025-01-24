package com.example.smamysuperalarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Model_Person::class], version = 1 , exportSchema = false)
abstract class PersonDatabase: RoomDatabase() {

    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: PersonDatabase? = null

        fun getDb(context: Context): PersonDatabase{
            val instance = INSTANCE
            if (instance != null) {
                return instance
            }
            synchronized(this){
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    PersonDatabase::class.java,
                    "person_database"
                ).build()
                INSTANCE = inst
                return inst
            }

        }
    }
}
