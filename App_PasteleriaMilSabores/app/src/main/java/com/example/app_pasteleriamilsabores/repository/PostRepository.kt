package com.example.app_pasteleriamilsabores.repository

import com.example.app_pasteleriamilsabores.data.model.Post
import com.example.app_pasteleriamilsabores.data.remote.RetrofitInstance

class PostRepository {

    suspend fun getPost(): List<Post>{
        return RetrofitInstance.api.getPost()
    }

}