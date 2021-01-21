package com.jetbrains.kmm.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val sports: List<Sport>
)