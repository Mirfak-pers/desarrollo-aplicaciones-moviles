package com.example.pasteleriamilsabores.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: Repository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _productState = MutableStateFlow<ProductState>(ProductState.Idle)
    val productState: StateFlow<ProductState> = _productState

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {
                _products.value = repository.getAllProducts()
                _productState.value = ProductState.Success
            } catch (e: Exception) {
                _productState.value = ProductState.Error(e.message ?: "Error al cargar productos")
            }
        }
    }

    fun loadProductsFromApi() {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {
                val apiProducts = repository.fetchProductsFromApi()
                _products.value = apiProducts
                _productState.value = ProductState.Success
            } catch (e: Exception) {
                _productState.value = ProductState.Error("Error al cargar productos de la API")
            }
        }
    }

    fun addProduct(title: String, price: String, description: String, image: String, userId: Long) {
        if (title.isBlank()) {
            _productState.value = ProductState.Error("Título requerido")
            return
        }
        val priceValue = price.toDoubleOrNull()
        if (priceValue == null || priceValue <= 0) {
            _productState.value = ProductState.Error("Precio inválido")
            return
        }

        viewModelScope.launch {
            _productState.value = ProductState.Loading
            val id = repository.addProduct(title, priceValue, description, image, userId)
            if (id > 0) {
                loadProducts()
            } else {
                _productState.value = ProductState.Error("Error al agregar producto")
            }
        }
    }

    fun updateProduct(id: Long, title: String, price: String, description: String, image: String) {
        if (title.isBlank()) {
            _productState.value = ProductState.Error("Título requerido")
            return
        }
        val priceValue = price.toDoubleOrNull()
        if (priceValue == null || priceValue <= 0) {
            _productState.value = ProductState.Error("Precio inválido")
            return
        }

        viewModelScope.launch {
            _productState.value = ProductState.Loading
            val success = repository.updateProduct(id, title, priceValue, description, image)
            if (success) {
                loadProducts()
            } else {
                _productState.value = ProductState.Error("Error al actualizar producto")
            }
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
            loadProducts()
        }
    }

    fun resetState() {
        _productState.value = ProductState.Idle
    }

    sealed class ProductState {
        object Idle : ProductState()
        object Loading : ProductState()
        object Success : ProductState()
        data class Error(val message: String) : ProductState()
    }
}