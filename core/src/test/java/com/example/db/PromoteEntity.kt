package com.example.db

import com.room.anno.Column
import com.room.anno.Entity

@Entity(tableName = "promote", unique = ["uuid"])
data class PromoteEntity(
    @Column(primaryKey = true, autoIncrement = true)
    var _id: Long = 0,
    var uuid: Long,//素材Id
     var name: String, //素材名称
     var note: String, //素材备注(备用)
      var type: Int, //素材类型(图片、视频)
      var source: Int, //素材类型来源
     var file: String, //文件名
     var size: Long, //文件大小 字节

     var status: Int,//下载状态 （未下载， 下载中， 已下载， 下载失败, 文件丢失, 逻辑删除）
    var count: Int,//重试次数
    var ctime: Long
) {
    constructor() : this(0, 0, "", "", 0, 0, "", -1, 0, 0, 0L)

}