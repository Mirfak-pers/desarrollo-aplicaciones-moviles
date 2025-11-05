package com.example.app_pasteleria_mil_sabores.ui.catalog


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.ProductoRepository
import com.example.app_pasteleria_mil_sabores.model.Producto
import kotlinx.coroutines.launch

class CatalogoViewModel(private val repository: ProductoRepository) : ViewModel() {

    val listaProductos = MutableLiveData<List<Producto>>(emptyList())
    val isLoading = MutableLiveData(false)

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        isLoading.value = true
        viewModelScope.launch {
            val productos = repository.obtenerCatalogo()
            listaProductos.value = productos
            isLoading.value = false
        }
    }
}