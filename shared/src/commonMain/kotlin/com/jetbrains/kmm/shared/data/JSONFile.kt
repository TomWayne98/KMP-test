package com.jetbrains.kmm.shared.data

import kotlinx.serialization.Serializable

@Serializable
data class JSONFile(
    val jsonrpc: Float = 2.0f,
    val result: Result
)