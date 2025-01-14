package com.example.smamysuperalarm.model;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Model_Person(
    @PrimaryKey(autoGenerate = false)

    val nume: String,

)

