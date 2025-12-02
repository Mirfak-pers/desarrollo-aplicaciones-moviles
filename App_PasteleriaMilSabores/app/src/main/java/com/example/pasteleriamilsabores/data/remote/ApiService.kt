package com.example.pasteleriamilsabores.data.remote

import com.example.pasteleriamilsabores.data.model.ApiProduct
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ApiProduct>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}