package com.jetbrains.kmm.shared

expect class Platform() {
    val platform: String

    fun getCurrentTimeInMilis(): Long
}