package com.jetbrains.kmm.shared.data

import com.jetbrains.kmm.data.SQLSport
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

fun Sport.toSQLItem(): SQLSport {
    return SQLSport(
        this.idSport.toLong(),
        this.sportName,
        this.sportCode,
        this.sportOrder.toLong()
    )
}