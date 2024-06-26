package com.example.db

import com.room.DatabaseBuilder
import com.room.anno.*
import org.slf4j.LoggerFactory

@Database(version = 1,
        entities = [
            User::class
        ])
interface AppDatabase {
    fun appDao(): AppDao
}

@Entity(tableName = "user", unique = ["name"])
data class User(
    @Column(primaryKey = true, autoIncrement = true) var id: Long = 0,
    var name: String
) {
    constructor():this(id = 0, name = "")

    override fun toString(): String {
        return "id=${id},name=${name}"
    }
}


@Dao
interface AppDao {
    @Query("select * from user")
    fun query(): List<User>

    @Query("select * from user WHERE id = ?")
    fun queryById(id: Long): User

    @Query("select COUNT(*) from user")
    fun count(): Int

    @Insert
    fun add(user: User): Boolean

    @Update
    fun update(user: User): Boolean

    @Delete
    fun delete(user: User): Boolean

    @Delete("DELETE FROM user WHERE name=?")
    fun delete(name: String): Boolean

    @Delete("DELETE FROM user WHERE name=?")
    fun delete2(name: String): Int

    fun hello() {
        println("hello")
    }
 }


object Main {
    private val logger = LoggerFactory.getLogger(DatabaseBuilder::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val database = DatabaseBuilder(AppDatabase::class.java).build()
        val dao: AppDao = database.appDao()
        dao.hello()
        logger.info("dao = ${dao}")
        val user = User(
                name = "john"
        )
        dao.add(user)
        dao.add(User(
                name = "admin"
        ))
        println("delete result ${dao.delete("john")}")
        println("delete result ${dao.delete2("admin")}")
        val list = dao.query()
        logger.info("result:$list,count=${dao.count()}")
        dao.update(list[0].apply {
            name = "jack"
        })
        logger.info("${dao.queryById(list[0]!!.id)}")
        logger.info("result:${dao.query()}")
        //dao.delete(list[0])
        logger.info("result:${dao.query()}")
    }
}