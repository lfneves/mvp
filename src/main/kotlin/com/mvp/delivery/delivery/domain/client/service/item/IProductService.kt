package com.mvp.delivery.delivery.domain.client.service.item

import com.mvp.delivery.delivery.infrastruture.entity.product.ProductEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IProductService {
    fun getProductById(id: Int): Mono<ProductEntity>
    fun saveProduct(productEntity: ProductEntity): Mono<ProductEntity>
    fun updateProduct(id: Int, productEntity: ProductEntity): Mono<ProductEntity>
    fun deleteProductById(id: Int): Mono<Void>
    fun getProducts(): Flux<ProductEntity>
    fun deleteAllProducts(): Mono<Void>
    fun getByCategory(name: String): Flux<ProductEntity>
}