package com.jetbrains.kmm.shared

import com.github.aakira.napier.Napier
import com.jetbrains.kmm.SomeDatabase
import com.jetbrains.kmm.data.SQLSport
import com.jetbrains.kmm.shared.data.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

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
            val result = downloadJSON()
            //android.util.Log.d("TOMW", ": Downloaded")
            val sports = result.result.sports
          //  Log.d("TOMW", ": Sport size ${sports.size}")
            val regions = mutableListOf<Region>()
            val leagues = mutableListOf<League>()
            val matches = mutableListOf<Match>()
            val odds = mutableListOf<Odd>()

            println("TEST")

            db.transaction {
                db.sportQueries.deleteAllItems()
                db.regionQueries.deleteAllItems()
                db.matchQueries.deleteAllItems()
                db.leagueQueries.deleteAllItems()
                db.oddQueries.deleteAllItems()
            }

            sports.forEach { sport ->
                regions.addAll(sport.regions)

                db.transaction {
                    this.afterCommit { Napier.d("TOMW",tag = "Sports inserted into DB ") }
                    this.afterRollback { Napier.d("TOMW",tag = "Sports insert into DB failed ") }
                    //db.sportQueries.insertAll(sport)
                    db.sportQueries.insert(sport.toSQLItem())
                }
            }

            regions.forEach { region ->
                leagues.addAll(region.leagues)

                db.transaction {
                    db.regionQueries.deleteAllItems()
                    this.afterCommit { Napier.d("TOMW",tag = "Region inserted into DB ") }
                    this.afterRollback { Napier.d("TOMW",tag = "Region insert into DB failed ") }
                    db.regionQueries.insert(region.toSQLItem(region.regionOrder))
                }
            }

            leagues.forEach { league ->
                matches.addAll(league.matches)
                db.transaction {
                    this.afterCommit { Napier.d("TOMW",tag = "League inserted into DB ") }
                    this.afterRollback { Napier.d("TOMW",tag = "League insert into DB failed ") }
                    db.leagueQueries.insert(league.toSQLItem(league.leagueId))
                }
            }

            matches.forEach { match ->
                odds.addAll(match.odds)
                db.transaction {
                    this.afterCommit { Napier.d("TOMW",tag = "Match inserted into DB ") }
                    this.afterRollback { Napier.d("TOMW",tag = "Match insert into DB failed ") }
                    db.matchQueries.insert(match.toSQLItem(match.opportunityId))
                }
            }

            odds.forEach { odd ->
                db.transaction {
                    this.afterCommit { Napier.d("TOMW",tag = "Odd inserted into DB ") }
                    this.afterRollback { Napier.d("TOMW",tag = "Odd insert into DB failed ") }
                    db.oddQueries.insert(odd.toSQLItem(odd.oddsId))
                }
            }
            Napier.d("TOMW NAPIER END ")
            println("TEST 1")
        }

        suspend fun downloadJSON() : JSONFile {
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