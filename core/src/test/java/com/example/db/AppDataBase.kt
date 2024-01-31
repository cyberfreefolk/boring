package com.example.db

import com.room.anno.Database

/**
 * 数据库文件
 */
@Database(
    entities = [UploadEntity::class, OtherRecord::class],
    version = 1)
interface AppDataBase  {
     fun uploadDao(): UploadDao
     fun otherDao(): OtherRecordDao
}