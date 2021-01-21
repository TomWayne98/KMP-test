package com.jetbrains.kmm.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sport(
    @SerialName("id_sport")
    val idSport: Int,
    @SerialName("sport_name")
    val sportName: String,
    @SerialName("sport_code")
    val sportCode: String,
    @SerialName("sport_order")
    val sportOrder: Int,
    val regions: List<Region>
)