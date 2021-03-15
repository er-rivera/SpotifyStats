package com.erivera.apps.topcharts.repository.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {
    private val cacheSize: Int = 10 * 1024 * 1024

    private const val BASE_URL = "https://api.spotify.com/"

    fun makeRetrofitService(clientId : String, context: Context): SpotifyService {

        val httpClient = OkHttpClient.Builder()

        httpClient.cache(
            Cache(context.cacheDir, cacheSize.toLong())
        )

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $clientId").build()
            chain.proceed(request)
        }


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build().create(SpotifyService::class.java)
    }
}