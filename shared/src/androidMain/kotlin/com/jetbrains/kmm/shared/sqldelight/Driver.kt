package com.jetbrains.kmm.shared.sqldelight

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jetbrains.kmm.SomeDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DbArgs(
    var context: Context
)

actual fun getSqlDriver(dbArgs: DbArgs): SqlDriver {
    return AndroidSqliteDriver(
        SomeDatabase.Schema,
        dbArgs.context,
        name = "test.db",
        callback = getFasterWritesCallback()
    )
}

private fun getFasterWritesCallback(): AndroidSqliteDriver.Callback {
    return object : AndroidSqliteDriver.Callback(SomeDatabase.Schema) {
        override fun onConfigure(db: SupportSQLiteDatabase) {
            super.onConfigure(db)
            setPragma(db, "JOURNAL_MODE = WAL")
            setPragma(db, "SYNCHRONOUS = 2")
        }

        private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
            val cursor = db.query("PRAGMA $pragma")
            cursor.moveToFirst()
            cursor.close()
        }
    }
}
