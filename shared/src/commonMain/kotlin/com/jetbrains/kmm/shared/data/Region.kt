package com.jetbrains.kmm.shared.data

import com.jetbrains.kmm.data.SQLReqion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Region(
    @SerialName("id_oppty_region")
    val idOpptyRegion: Int,
    @SerialName("oppty_region_name")
    val opptyRegionName: String,
    @SerialName("oppty_region_code")
    val opttyRegionCode: String? = null,
    @SerialName("region_order")
    val regionOrder: Int,
    val leagues: List<League>
)

fun Region.toSQLItem(parentSportId: Int): SQLReqion {
    return SQLReqion(
        parentSportId.toLong(),
        this.idOpptyRegion.toLong(),
        this.opptyRegionName,
        this.opttyRegionCode.orEmpty(),
        this.regionOrder.toLong()
    )
}
