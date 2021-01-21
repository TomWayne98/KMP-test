package com.jetbrains.kmm.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DBFileRequestBodyParams(val os: String = "ANDROID", val version: String = "")
