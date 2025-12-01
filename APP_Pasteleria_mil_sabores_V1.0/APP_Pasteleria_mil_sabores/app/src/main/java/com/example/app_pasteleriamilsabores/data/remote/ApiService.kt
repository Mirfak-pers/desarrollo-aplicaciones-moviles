package com.example.app_pasteleriamilsabores.data.remote
import com.example.app_pasteleriamilsabores.data.model.Post
import retrofit2.http.GET
import javax.annotation.processing.Generated


interface ApiService {
    @GET("/posts")
    suspend fun getPost(): List<Post>

}