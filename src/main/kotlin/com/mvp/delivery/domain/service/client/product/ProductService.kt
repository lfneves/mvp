package com.mvp.delivery.domain.service.client.product

import com.mvp.delivery.domain.model.product.ProductDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface ProductService {

    fun getProductById(id: Int): Mono<ProductDTO>

    fun getProducts(): Flux<ProductDTO>

    fun getProductsByCategoryByName(name: String): Flux<ProductDTO>

    fun getAllById(id: List<Long?>): Flux<ProductDTO>

    fun getByIdTotalPrice(ids: List<Long?>): Mono<BigDecimal>
}