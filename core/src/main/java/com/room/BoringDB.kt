package com.room

import com.room.anno.*
import com.room.handlers.BeanHandler
import com.room.handlers.BeanListHandler
import com.room.handlers.ColumnListHandler
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.sql.ResultSet


class DatabaseBuilder<T>(private val clazz: Class<T>) {

    private val logger = LoggerFactory.getLogger(DatabaseBuilder::class.java)

    var path: String = "boring.db"
    fun build(): T {
        return Proxy.newProxyInstance(
                clazz.classLoader,
                arrayOf(clazz),
                object : InvocationHandler {
                    private val o = Object()
                    private val empty: Array<Any> = emptyArray()
                    private val dbSession: DBSession

                    init {
                        val annotation = clazz.getAnnotation(Database::class.java)
                        dbSession = DBSession(path, *(annotation?.entities ?: emptyArray()))
                    }

                    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
                        if (method.declaringClass == Object::class.java) {
                            return method.invoke(this, *(args ?: empty))
                        }
                        val returnType = method.returnType
                        //method.genericReturnType
                        if (returnType.isAnnotationPresent(Dao::class.java)) {
                            logger.info("创建代理Dao对象: ${returnType}")
                            return DaoBuilder(returnType)
                                    .session(dbSession)
                                    .build()
                        }
                        return null
                    }
                }) as T
    }
}

class DaoBuilder<T : @Dao Any>(private val clazz: Class<T>) {
    private var mSession: DBSession? = null
    private val logger = LoggerFactory.getLogger(DatabaseBuilder::class.java)

    fun session(session: DBSession): DaoBuilder<T> {
        mSession = session
        return this
    }

    fun build(): T {
        if (mSession == null) {
            throw IllegalStateException("session == null")
        }

        return Proxy.newProxyInstance(
                clazz.classLoader,
                arrayOf(clazz),
                object : InvocationHandler {
                    val empty: Array<Any> = emptyArray()

                    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
                        if (method.declaringClass == Object::class.java) {
                            return method.invoke(this, *(args ?: empty))
                        }
                        val annotations = method.annotations
                        if (annotations.isNullOrEmpty()) {
                            if(method.isDefault) {

                                // the args array can be null if the number of formal parameters required by
                                // the method is zero (consistent with Method::invoke)

                                //return InvocationHandler.invokeDefault(proxy, method, *(args ?: empty))
                                try {
                                    val caller = InvocationHandler::class.java.getMethod("invokeDefault", Any::class.java, Method::class.java, Array<Any>::class.java)

                                    return caller.invoke(null, proxy, method, (args ?: empty))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    logger.error("dao中不被注解的方法不被处理")
                                    return null
                                }

                            } else {
                                logger.error("dao中不被注解的方法不被处理")
                                return null
                            }
                        }
                        if (annotations.size > 1) {
                            logger.error("dao中一个方法只允许被一个注解")
                        }

                        val ano = annotations[0]
                        logger.info("invoke:${method}->${args}")
                        when (ano) {
                            is Insert -> {
                                return performInsert(args!![0], ano)
                            }
                            is Delete -> {
                                if (ano.value.isNullOrEmpty()) {
                                    // 需要primaryKey
                                    val performDelete = performDelete(args!![0], ano)
                                    if (method.returnType.isAssignableFrom(Boolean::class.java)) {
                                        return performDelete > 0
                                    } else {
                                        return performDelete
                                    }
                                } else {
                                    if (method.returnType.isAssignableFrom(Boolean::class.java)) {
                                        return mSession!!.update0(ano.value, *(args ?: empty)) > 0
                                    } else {
                                        return mSession!!.update0(ano.value, *(args ?: empty))
                                    }
                                }

                            }
                            is Update -> {
                                if (method.returnType.isAssignableFrom(Boolean::class.java)) {
                                    return performUpdate(args!![0], ano) > 0
                                } else {
                                    return performUpdate(args!![0], ano)
                                }
                            }
                            is Query -> {
                                val sql = ano.value
                                val resolver = when {
                                    //由于我们不是用代码生成，这里只能枚举
                                    method.returnType.isAssignableFrom(Int::class.java) -> {
                                        IntRsResolver()
                                    }
                                    method.returnType.isAssignableFrom(Long::class.java) -> {
                                        LongRsResolver()
                                    }
                                    method.returnType.isAssignableFrom(Boolean::class.java) -> {
                                        BooleanRsResolver()
                                    }
                                    method.returnType.isAssignableFrom(String()::class.java) -> {
                                        StringRsResolver()
                                    }
                                    method.returnType.isAssignableFrom(List::class.java) -> {
                                        val gr = method.genericReturnType as ParameterizedType

                                        val clazz1 = Class.forName(gr.actualTypeArguments[0].typeName)
                                        if (clazz1.isPrimitive || clazz1 == String::class.java) {
                                            ArrayListResolver(clazz1)
                                        } else {
                                            ListRsResolver(clazz1)
                                        }
                                    }
                                    else -> {
                                        ObjRsResolver(method.returnType)
                                    }
                                }
                                return mSession!!.query(sql, resolver, *(args ?: empty))
                            }
                        }
                        return null
                    }
                }) as T
    }


    private fun performInsert(entity: Any?, annotation: Annotation): Boolean {
        return mSession!!.execInsert( entity!!, annotation)
    }

    private fun performDelete(entity: Any?, annotation: Annotation): Int {
        return mSession!!.execDelete( entity!!, annotation)
    }

    private fun performUpdate(entity: Any?, annotation: Annotation): Int {
        return mSession!!.execUpdate( entity!!, annotation)
    }
}

class IntRsResolver : ResultSetResolver<Int> {
    override fun resolve(rs: ResultSet): Int {
        return rs.getInt(1)
    }
}

class LongRsResolver : ResultSetResolver<Long> {
    override fun resolve(rs: ResultSet): Long {
        return rs.getLong(1)
    }
}

class BooleanRsResolver : ResultSetResolver<Boolean> {
    override fun resolve(rs: ResultSet): Boolean {
        return rs.getBoolean(1)
    }
}

class StringRsResolver : ResultSetResolver<String> {
    override fun resolve(rs: ResultSet): String {
        return rs.getString(1)
    }
}

class ListRsResolver<T>(val clazz: Class<T>) : ResultSetResolver<List<T>> {
    val handler: BeanListHandler<T> by lazy {
        BeanListHandler(
            clazz
        )
    }

    override fun resolve(rs: ResultSet): List<T> {
        return handler.handle(rs)
    }
}

class ArrayListResolver<T>(val clazz: Class<T>) : ResultSetResolver<List<T>> {
    val handler: ColumnListHandler<T> by lazy { ColumnListHandler() }

    override fun resolve(rs: ResultSet): List<T> {
        return handler.handle(rs) as List<T>
    }
}
class ObjRsResolver<T>(val clazz: Class<T>) : ResultSetResolver<T> {
    val handler: BeanHandler<T> by lazy {
        BeanHandler(
            clazz
        )
    }

    override fun resolve(rs: ResultSet): T {
        return handler.handle(rs)
    }
}