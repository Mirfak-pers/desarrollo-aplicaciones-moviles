package com.example.app_pasteleriamisabores.repository

import com.example.app_pasteleriamisabores.data.model.Post
import com.example.app_pasteleriamisabores.data.remote.ApiService
import com.example.app_pasteleriamisabores.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

// Clase testable para inyección manual
class TestablePostRepository(private val testApi: ApiService) : PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApi.getPosts()
    }
}

class PostRepositoryTest : StringSpec({
    "getPosts() debe retornar una lista de posts simulada" {
        // 1. Simulamos el resultado de la API
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Cuerpo 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Cuerpo 2")
        )

        // 2. Creamos un mock de ApiService
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts

        // 3. Usamos la clase de test inyectando el mock
        val repo = TestablePostRepository(testApi = mockApi)

        // 4. Ejecutamos el test
        runTest {
            val result = repo.getPosts()
            result shouldContainExactly fakePosts
        }
    }
})