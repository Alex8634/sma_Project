package com.example.smamysuperalarm.data;

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_table")
data class Model_Person(
    @PrimaryKey(autoGenerate = false)

    val nume: String,
    val code: String

)

