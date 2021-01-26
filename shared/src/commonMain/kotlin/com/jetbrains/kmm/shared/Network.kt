package com.jetbrains.kmm.shared

import com.github.aakira.napier.Napier
import com.jetbrains.kmm.SomeDatabase
import com.jetbrains.kmm.shared.data.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class Network {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun downloadDB(): DBFile {
        return httpClient.post<DBFile>(DB_ENDPOINT) {
            // When you send body in form of data class (serialized to JSON) you need to add this header
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = DBFileRequestBody()
        }
    }

    suspend fun parsePrematchJSON(db: SomeDatabase) {
        var dbWriteTimeStart = 0L
        var dbDeleteTime = 0L
        var dbSportWrittenTime = 0L
        var dbRegionsWrittenTime = 0L
        var dbLeaguesWrittenTime = 0L
        var dbMatchWrittenTime = 0L
        var dbOddsWrittenTime = 0L
        var dbWriteTimeEnd = 0L


        val result = downloadJSON()
        //android.util.Log.d("TOMW", ": Downloaded")
        val sports = result.result.sports
        //  Log.d("TOMW", ": Sport size ${sports.size}")
        val regions = mutableListOf<Region>()
        val leagues = mutableListOf<League>()
        val matches = mutableListOf<Match>()
        val odds = mutableListOf<Odd>()

        Napier.d("DB Write starts")
        dbWriteTimeStart = Platform().getCurrentTimeInMilis()

        db.transaction {
            db.sportQueries.deleteAllItems()
            db.regionQueries.deleteAllItems()
            db.matchQueries.deleteAllItems()
            db.leagueQueries.deleteAllItems()
            db.oddQueries.deleteAllItems()
            afterCommit {
                dbDeleteTime = Platform().getCurrentTimeInMilis() - dbWriteTimeStart
            }
        }

        sports.forEach { sport ->
            regions.addAll(sport.regions)

            db.transaction {
                this.afterCommit {
                    dbSportWrittenTime =
                        Platform().getCurrentTimeInMilis() - (dbDeleteTime + dbWriteTimeStart)
                    //Napier.d("TOMW",tag = "Sports inserted into DB ")
                }
                //this.afterRollback { Napier.d("TOMW",tag = "Sports insert into DB failed ") }
                //db.sportQueries.insertAll(sport)
                db.sportQueries.insert(sport.toSQLItem())
            }
        }
        regions.forEach { region ->
            leagues.addAll(region.leagues)

            db.transaction {
                this.afterCommit {
                    // Napier.d("TOMW",tag = "Region inserted into DB ")
                    dbRegionsWrittenTime =
                        Platform().getCurrentTimeInMilis() - (dbDeleteTime + dbWriteTimeStart + dbSportWrittenTime)
                }
                // this.afterRollback { Napier.d("TOMW",tag = "Region insert into DB failed ") }
                db.regionQueries.insert(region.toSQLItem(region.regionOrder))
            }
        }

        leagues.forEach { league ->
            matches.addAll(league.matches)
            db.transaction {
                this.afterCommit {
                    dbLeaguesWrittenTime =
                        Platform().getCurrentTimeInMilis() - (dbDeleteTime + dbWriteTimeStart + dbSportWrittenTime + dbRegionsWrittenTime)
                }
                // Napier.d("TOMW",tag = "League inserted into DB ") }
                //this.afterRollback { Napier.d("TOMW",tag = "League insert into DB failed ") }
                db.leagueQueries.insert(league.toSQLItem(league.leagueId))
            }
        }

        matches.forEach { match ->
            odds.addAll(match.odds)
            db.transaction {
                this.afterCommit {
                    // Napier.d("TOMW",tag = "Match inserted into DB ")
                    dbMatchWrittenTime =
                        Platform().getCurrentTimeInMilis() - (dbDeleteTime + dbWriteTimeStart + dbSportWrittenTime + dbRegionsWrittenTime + dbLeaguesWrittenTime)
                }
                //this.afterRollback { Napier.d("TOMW",tag = "Match insert into DB failed ") }
                db.matchQueries.insert(match.toSQLItem(match.opportunityId))
            }
        }

        odds.forEach { odd ->
            db.transaction {
                this.afterCommit {
                    dbOddsWrittenTime =
                        Platform().getCurrentTimeInMilis() - (dbDeleteTime + dbWriteTimeStart + dbSportWrittenTime + dbRegionsWrittenTime + dbLeaguesWrittenTime + dbMatchWrittenTime)
                    // Napier.d("TOMW",tag = "Odd inserted into DB ") }
                    //    this.afterRollback { Napier.d("TOMW",tag = "Odd insert into DB failed ") }
                    db.oddQueries.insert(odd.toSQLItem(odd.oddsId))
                }
            }
        }

        dbWriteTimeEnd = Platform().getCurrentTimeInMilis() - dbWriteTimeStart
        Napier.d("TOMW NAPIER END it took ${dbWriteTimeEnd} ")
        Napier.d("TOMW DB transactions took - deleting: $dbDeleteTime sports: $dbSportWrittenTime regions: $dbRegionsWrittenTime leagues: $dbLeaguesWrittenTime matches: $dbMatchWrittenTime odds: $dbOddsWrittenTime ")
        println("TEST 1")
    }

    suspend fun downloadJSON(): JSONFile {
        return httpClient.post<JSONFile>(DB_ENDPOINT) {
            // When you send body in form of data class (serialized to JSON) you need to add this header
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = JSONRequestBody()
        }
    }


    companion object {
        private const val DB_ENDPOINT = "https://mapi.sts.pl/"
    }
}