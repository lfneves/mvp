package com.mvp.delivery.domain.service.admin.product

import com.mvp.delivery.domain.model.product.CategoryDTO
import com.mvp.delivery.domain.model.product.ProductDTO
import com.mvp.delivery.domain.model.product.ProductRequestDTO
import org.springframework.cache.annotation.CachePut
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductAdminService {
    fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO>

    fun deleteProductById(id: Int): Mono<Void>

    fun getProducts(): Flux<ProductDTO>

    fun getAllCategory(): Flux<CategoryDTO>

    fun deleteAllProducts(): Mono<Void>

    @CachePut(cacheNames = ["productsCache"])
    fun updateProduct(id: Int, productRequestDTO: ProductRequestDTO): Mono<ProductDTO>
}