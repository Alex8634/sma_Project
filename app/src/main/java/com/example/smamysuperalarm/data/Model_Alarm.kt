package com.example.smamysuperalarm.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alarm_database")
data class Model_Alarm(
    @PrimaryKey(autoGenerate = true)
    var alarmId : Int

)
