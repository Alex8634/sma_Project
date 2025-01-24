package com.example.smamysuperalarm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)//ignora entryurile identice
    suspend fun addPerson(person: Model_Person)

    @Query("SELECT * FROM person_table")
    fun getAll(): List<Model_Person>


    @Query("SELECT * FROM person_table WHERE nume = :nume")
    fun getPersonByName(nume: String): Model_Person

}