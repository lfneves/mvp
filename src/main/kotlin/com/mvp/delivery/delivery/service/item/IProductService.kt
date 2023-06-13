package com.mvp.delivery.delivery.service.item

import com.mvp.delivery.delivery.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IProductService {
    fun getProductById(id: Int): Mono<Product>
    fun saveProduct(product: Product): Mono<Product>
    fun updateProduct(id: Int, product: Product): Mono<Product>
    fun deleteProductById(id: Int): Mono<Void>
    fun getProducts(): Flux<Product>
    fun deleteAllProducts(): Mono<Void>
    fun getByCategory(name: String): Flux<Product>
}