package com.jetbrains.kmm.shared.data

import com.jetbrains.kmm.data.SQLLeague
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

fun League.toSQLItem(parentRegionId: Int): SQLLeague {
   return SQLLeague(
        parentRegionId.toLong(),
        leagueId.toLong(),
        leagueName,
        eventNote
    )
}
