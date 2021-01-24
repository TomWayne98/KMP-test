package com.jetbrains.kmm.shared.sqldelight

import com.jetbrains.kmm.SomeDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver

actual class DbArgs

actual fun getSqlDriver(dbArgs: DbArgs): SqlDriver {
    // It show that NativeSqliteDriver is not visible but it is actually - it is just IDE error
    val driver: SqlDriver = NativeSqliteDriver(SomeDatabase.Schema, "test.db")
    return driver
}
