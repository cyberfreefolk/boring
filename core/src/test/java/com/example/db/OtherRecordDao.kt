package com.example.db

import com.room.anno.*

@Dao
interface OtherRecordDao {
    @Insert
    fun insert(record: OtherRecord)

    @Update
    fun update(record: OtherRecord)

    @Query("SELECT  *  FROM other_record")
    fun getRecords(): List<OtherRecord>

//    @Query("SELECT  *  FROM other_record WHERE type IN (:types)  ORDER BY drillAt DESC")
//    fun getRecordsData(@AlertType vararg types: Int): DataSource.Factory<Int, OtherRecord>

    @Query("SELECT  *  FROM other_record WHERE syncState = 0 and type IN (:types)")
    fun getUnSyncRecords( vararg types: Int): List<OtherRecord>
    /**
     * 删除全部
     */
    @Delete("DELETE FROM other_record WHERE type IN (?,?,?)")
    fun deleteAll( vararg types: Int)

}