package com.mvp.delivery.domain.admin.product

import com.mvp.delivery.domain.client.model.product.ProductDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductAdminService {
    fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO>

    fun updateProduct(id: Int, productDTO: ProductDTO): Mono<ProductDTO>

    fun deleteProductById(id: Int): Mono<Void>

    fun getProducts(): Flux<ProductDTO>

    fun deleteAllProducts(): Mono<Void>
}