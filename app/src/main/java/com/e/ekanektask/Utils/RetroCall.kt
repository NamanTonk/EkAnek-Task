package com.e.ekanektask.Utils

import android.util.Base64
import com.e.ekanektask.model.ImagesModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class RetroCall {

    companion object {
        var CallAPi = Retrofit.Builder().baseUrl(String(Base64.decode( "aHR0cHM6Ly9waXhhYmF5LmNvbQ==",Base64.DEFAULT), Charset.forName("UTF-8"))).client(createIntercepter())
            .addConverterFactory(GsonConverterFactory.create()).build().create(Api::class.java)
        fun createIntercepter(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }
    }
    interface Api {
        @GET("/api/?key=5991727-868f45466b71ba672cc7a5bea")
        fun getImages(
            @Query("q") search: String,
            @Query("pretty") pretty: Boolean,
            @Query("page") page: Int
        ): Call<ImagesModel>
    }
}