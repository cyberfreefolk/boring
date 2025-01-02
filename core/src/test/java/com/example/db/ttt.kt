package com.example.db

import com.room.DatabaseBuilder

object ttt {
    @JvmStatic
    fun main(args: Array<String>) {
        val database = DatabaseBuilder(AppDataBase::class.java).apply {
            path = "eew.db"
        } .build()
        val dao: PromoteDao = database.promoteDao()
        val list = dao.queryAll(1)
        list.forEach {
            println(it)
        }
    }
}
