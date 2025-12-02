package com.example.pasteleriamilsabores

import com.example.pasteleriamilsabores.data.model.CartItem
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelKotestTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    lateinit var repository: Repository
    lateinit var viewModel: CartViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        repository = mockk()
        viewModel = CartViewModel(repository)
    }

    given("un CartViewModel") {
        `when`("se carga el carrito con items") {
            then("debe actualizar la lista de items") {
                runTest {
                    // Given
                    val cartItems = listOf(
                        CartItem(1, Product(1, "Producto 1", 10.0), 2),
                        CartItem(2, Product(2, "Producto 2", 20.0), 1)
                    )
                    coEvery { repository.getCartItems(1L) } returns cartItems

                    // When
                    viewModel.loadCart(1L)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    viewModel.cartItems.value shouldBe cartItems
                    viewModel.cartState.value.shouldBeInstanceOf<CartViewModel.CartState.Success>()
                }
            }
        }

        `when`("se calcula el precio total") {
            then("debe sumar correctamente todos los items") {
                runTest {
                    // Given
                    val cartItems = listOf(
                        CartItem(1, Product(1, "Producto 1", 10.0), 2),
                        CartItem(2, Product(2, "Producto 2", 20.0), 1)
                    )
                    coEvery { repository.getCartItems(1L) } returns cartItems
                    viewModel.loadCart(1L)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // When
                    val total = viewModel.getTotalPrice()

                    // Then
                    total shouldBe 40.0 // (10*2) + (20*1)
                }
            }
        }

        `when`("se agrega un producto al carrito") {
            then("debe llamar al repositorio y recargar") {
                runTest {
                    // Given
                    coEvery { repository.addToCart(1L, 1L, 1) } returns 1L
                    coEvery { repository.getCartItems(1L) } returns emptyList()

                    // When
                    viewModel.addToCart(1L, 1L, 1)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    coVerify { repository.addToCart(1L, 1L, 1) }
                    coVerify { repository.getCartItems(1L) }
                }
            }
        }

        `when`("se elimina un item del carrito") {
            then("debe removerlo y recargar") {
                runTest {
                    // Given
                    coEvery { repository.removeFromCart(1L) } returns true
                    coEvery { repository.getCartItems(1L) } returns emptyList()

                    // When
                    viewModel.removeFromCart(1L, 1L)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    coVerify { repository.removeFromCart(1L) }
                    coVerify { repository.getCartItems(1L) }
                }
            }
        }

        `when`("ocurre un error al cargar el carrito") {
            then("debe actualizar el estado a Error") {
                runTest {
                    // Given
                    coEvery { repository.getCartItems(1L) } throws Exception("Database error")

                    // When
                    viewModel.loadCart(1L)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    viewModel.cartState.value.shouldBeInstanceOf<CartViewModel.CartState.Error>()
                }
            }
        }
    }
})