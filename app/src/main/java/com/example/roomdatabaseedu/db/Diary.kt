package com.example.roomdatabaseedu.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val idx: Int = 0,

    @ColumnInfo("date")
    val date: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("content")
    val content: String
)
