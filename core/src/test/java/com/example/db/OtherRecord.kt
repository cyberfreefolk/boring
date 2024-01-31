package com.example.db

import com.room.anno.Column
import com.room.anno.Entity

/**
 * 目前有的　消防演习、　科普(地震、消防), 又分远程、本地
 */
@Entity(tableName = "other_record")
data class OtherRecord(
    @Column(primaryKey = true, autoIncrement = false)
    var uuid: Long,
    var drillAt: Long,
    var duration: Long,
    var type: Int,
    var source: Int,
    var subType: Int = 0,
    var syncState: Int = 0    //0 未与服务器同步 1 已同步
) {
    constructor(): this(0, 0, 0, 0, 0, 0,0)
}