package com.jetbrains.kmm.shared.sqldelight

import android.content.Context
import com.jetbrains.kmm.SomeDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DbArgs(
    var context: Context
)

actual fun getSqlDriver(dbArgs: DbArgs): SqlDriver {
    val driver: SqlDriver = AndroidSqliteDriver(SomeDatabase.Schema, dbArgs.context, "test.db")
    return driver
}
