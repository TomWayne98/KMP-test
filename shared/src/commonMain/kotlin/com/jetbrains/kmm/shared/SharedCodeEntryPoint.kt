package com.jetbrains.kmm.shared

import com.jetbrains.kmm.SomeDatabase
import com.jetbrains.kmm.shared.sqldelight.DbArgs
import com.jetbrains.kmm.shared.sqldelight.getSqlDriver

class SharedCodeEntryPoint {

    private var db: SomeDatabase? = null

    fun prepareDb(dbArgs: DbArgs) {
        db = DatabaseCreator.getDataBase(dbArgs)
    }

    suspend fun savePrematchJSONtoDB() {
        initLogger()
        Network().parsePrematchJSON(db!!)
    }

    private fun initLogger() {
        Logger().enable()
    }
}



object DatabaseCreator {
    fun getDataBase(dbArgs: DbArgs): SomeDatabase? {
        val sqlDriver  = getSqlDriver(dbArgs)
        if (sqlDriver != null) {
            return SomeDatabase(sqlDriver)
        } else {
            return null
        }
    }
}