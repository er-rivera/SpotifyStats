package com.erivera.apps.topcharts.repository

import java.lang.Exception
import java.io.*
import com.google.gson.Gson

class JsonResourceObjectMapper<T>(private val model: Class<T>) {
    @Throws(IOException::class)
    fun getObjectFromFile(fileName: String?): T {
        var result: T? = null
        try {
            val inputStream: InputStream? =
                this.javaClass.classLoader?.getResourceAsStream(fileName)
            val gson = Gson()
            val reader = BufferedReader(InputStreamReader(inputStream))
            result = gson.fromJson(reader, model)
        } catch (exception: Exception) {
            println(exception.message)
        }
        return result!!
    }
}