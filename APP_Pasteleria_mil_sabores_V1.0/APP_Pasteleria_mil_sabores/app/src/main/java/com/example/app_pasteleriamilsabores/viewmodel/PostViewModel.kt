package com.example.app_pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleriamilsabores.data.model.Post
import com.example.app_pasteleriamilsabores.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _postList = MutableStateFlow<List<Post>>(emptyList())

    val postList: StateFlow<List<Post>> = _postList

    init {
        fetchPosts()
    }

    private fun fetchPosts(){
        viewModelScope.launch {
            try {
                _postList.value = repository.getPost()
            } catch (e: Exception){
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }

    }
}