package com.mvp.delivery.domain.client.service.product

import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface IProductService {

    fun getProductById(id: Int): Mono<ProductDTO>

    fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO>

    fun updateProduct(id: Int, productDTO: ProductDTO): Mono<ProductDTO>

    fun deleteProductById(id: Int): Mono<Void>

    fun getProducts(): Flux<ProductDTO>

    fun deleteAllProducts(): Mono<Void>

    fun getProductsByCategoryByName(name: String): Flux<ProductDTO>

    fun getAllById(id: List<Long?>): Flux<ProductDTO>

    fun getByIdTotalPrice(ids: List<Long?>): Mono<BigDecimal>
}