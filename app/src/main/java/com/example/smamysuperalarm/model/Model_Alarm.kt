package com.example.smamysuperalarm.model;
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity()
data class Model_Alarm(
    @PrimaryKey(autoGenerate = false)

    val nume: String,

    val snoozeType: String,

    val clock: Date


)
