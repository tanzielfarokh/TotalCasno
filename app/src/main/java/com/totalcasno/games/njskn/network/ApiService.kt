package com.totalcasno.games.njskn.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL as NetworkAddress

object ApiService {
    
    suspend fun fetchServerResponse(requestLink: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val address = NetworkAddress(requestLink)
            val connection = address.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                
                Result.success(response.toString())
            } else {
                Result.failure(Exception("Server returned code: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun parseResponse(response: String): Pair<String?, String?> {
        return if (response.contains("#")) {
            val parts = response.split("#", limit = 2)
            Pair(parts[0], parts.getOrNull(1))
        } else {
            Pair(null, null)
        }
    }
}

