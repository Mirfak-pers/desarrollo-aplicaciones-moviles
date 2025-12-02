package com.example.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.model.CartItem
import com.example.pasteleriamilsabores.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartState = MutableStateFlow<CartState>(CartState.Idle)
    val cartState: StateFlow<CartState> = _cartState

    fun loadCart(userId: Long) {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            try {
                _cartItems.value = repository.getCartItems(userId)
                _cartState.value = CartState.Success
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Error al cargar carrito")
            }
        }
    }

    fun addToCart(userId: Long, productId: Long, quantity: Int = 1) {
        viewModelScope.launch {
            val id = repository.addToCart(userId, productId, quantity)
            if (id > 0) {
                loadCart(userId)
            }
        }
    }

    fun removeFromCart(cartItemId: Long, userId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
            loadCart(userId)
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }

    sealed class CartState {
        object Idle : CartState()
        object Loading : CartState()
        object Success : CartState()
        data class Error(val message: String) : CartState()
    }
}