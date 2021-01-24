package com.jetbrains.kmm.shared.sqldelight

import com.squareup.sqldelight.db.SqlDriver

expect class DbArgs

expect fun getSqlDriver(dbArgs: DbArgs): SqlDriver