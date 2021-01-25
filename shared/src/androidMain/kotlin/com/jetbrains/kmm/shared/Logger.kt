package com.jetbrains.kmm.shared

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier

actual class Logger actual constructor() {
    actual fun enable() {
        Napier.base(DebugAntilog())
    }
}