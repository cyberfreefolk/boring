package com.example.db

import com.room.anno.Dao
import com.room.anno.Insert
import com.room.anno.Query
import com.room.anno.Update

@Dao
interface UploadDao {
    @Insert
    fun insert(uploadEntity: UploadEntity)
    @Query("SELECT  *  FROM quake_record WHERE syncState = 0")
    fun getUnSyncQuakes(): List<UploadEntity>

    @Query("SELECT  *  FROM quake_record")
    fun getAll(): List<UploadEntity>

    @Query("SELECT  *  FROM quake_record WHERE eventId=:eventId ORDER BY updates")
    fun get(eventId: Long): List<UploadEntity>

    @Update
    fun update(vararg uploadEntity: UploadEntity)

    @Query("DELETE  FROM quake_record WHERE eventId=:eventId")
    fun delete(eventId: Long)
}
