package com.jetbrains.kmm.shared

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

        suspend fun updatePrematchJSON() {
            val result = downloadJSON()
            //android.util.Log.d("TOMW", ": Downloaded")
            val sports = result.result.sports
          //  Log.d("TOMW", ": Sport size ${sports.size}")
            val regions = mutableListOf<Region>()
            val leagues = mutableListOf<League>()
            val matches = mutableListOf<Match>()
            val odds = mutableListOf<Odd>()

            sports.forEach { it ->
            //    android.util.Log.d("TOMW", ": Region: ${it.regions}")
                regions.addAll(it.regions)
            }

            regions.forEach {
                leagues.addAll(it.leagues)
            }

            leagues.forEach {
                matches.addAll(it.matches)
            }

            matches.forEach {
                odds.addAll(it.odds)
            }

            //android.util.Log.d("TOMW", "We parsed like: ${odds.size} odds")

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