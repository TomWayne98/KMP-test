package com.jetbrains.kmm.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class League(
    @SerialName("id_league")
    val leagueId: Int,
    @SerialName("league_name")
    val leagueName: String,
    @SerialName("event_note")
    val eventNote: String? = null,
    // TODO: count of sidebets is ignored
    //@SerialName("count_of_sidebets")
    val matches: List<Match>
)
