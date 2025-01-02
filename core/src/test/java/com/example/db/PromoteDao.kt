package com.example.db

import com.room.anno.*

@Dao
interface PromoteDao {
    @Insert
    fun insert(entity: PromoteEntity)

    @Update
    fun update(entity: PromoteEntity)

    @Delete
    fun delete(entity: PromoteEntity)


    @Query("SELECT  *  FROM promote WHERE  uuid = ?")
    fun queryById(id: Long): PromoteEntity?

    @Query("SELECT  *  FROM promote WHERE  type = ? ORDER BY _id DESC")
    fun queryAll(type: Int): List<PromoteEntity>





}