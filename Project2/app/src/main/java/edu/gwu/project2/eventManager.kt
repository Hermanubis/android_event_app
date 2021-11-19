package edu.gwu.project2

import android.location.Address
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class eventManager {
    val okHttpClient: OkHttpClient

    init {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }

    fun retrieveEvents(eventAPI: String, term: String): List<event> {
        val eventList: MutableList<event> = mutableListOf()

        // Unlike normal API Keys (like Google Maps and News API) Twitter uses something slightly different,
        // so the "apiKey" here isn't really an API Key - we'll see in Lecture 7.

        var request: Request =
            Request.Builder()
                .url("https://app.ticketmaster.com/discovery/v2/events.json?keyword=${term}&countryCode=US&apikey=$eventAPI")
                .header("Authorization", "$eventAPI")
                .build()


        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val events: JSONArray = json.getJSONObject("_embedded").getJSONArray("events")

            for (i in 0 until events.length()) {
                val curr: JSONObject = events.getJSONObject(i)
                val name = curr.getString("name")
                val time = curr.getJSONObject("dates").getJSONObject("start").getString("localDate")
                val urlToImage = curr.getString("urlToImage")
                val content = curr.getString("content")
                val url = curr.getString("url")
                val info = curr.getString("info")
                eventList.add(event(name, time, urlToImage, content, url, info))
            }
        }

        return eventList
    }
}