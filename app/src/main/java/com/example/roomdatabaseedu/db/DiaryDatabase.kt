package com.example.roomdatabaseedu.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Diary::class], version = 1, exportSchema = true)
abstract class DiaryDatabase: RoomDatabase() {
    abstract fun getDiaryDAO(): DiaryDAO
}