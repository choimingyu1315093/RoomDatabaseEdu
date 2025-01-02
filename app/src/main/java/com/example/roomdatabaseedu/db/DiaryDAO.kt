package com.example.roomdatabaseedu.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDAO {
    @Query("SELECT * FROM diary")
    suspend fun getAllDiary(): List<Diary>

    @Query("SELECT * FROM diary WHERE idx = :idx")
    suspend fun getDiary(idx: Int): Diary

    @Insert
    suspend fun saveDiary(diary: Diary)

    @Update
    suspend fun updateDiary(diary: Diary)

    @Delete
    suspend fun deleteDiary(diary: Diary)
}