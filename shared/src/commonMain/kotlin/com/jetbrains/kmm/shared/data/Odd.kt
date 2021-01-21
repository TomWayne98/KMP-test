package com.jetbrains.kmm.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Odd(
    @SerialName("tip_name")
    val tipName: String? = null,
    @SerialName("tip_shortname")
    val tipShortname: String? = null,
    @SerialName("odds_value")
    val oddsValue: Float,
    @SerialName("odds_value_numeric")
    val oddsValueNumber: Float,
    @SerialName("id_odds")
    val oddsId: Int,
    @SerialName("id_tip")
    val tipId: Int,
    @SerialName("in_ticket")
    val inTicket: Boolean
)