package com.mvp.delivery.infrastruture.repository.product

import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface IProductRepository : ReactiveCrudRepository<ProductEntity?, Long?> {
    @Query("SELECT id, name, price, quantity FROM tb_product WHERE name = $1")
    fun findByName(name: String?): Flux<ProductEntity>

    @Query("SELECT id, name, price, quantity, id_category FROM tb_product WHERE id_category = $1")
    fun findByIdCategory(id: Long?): Flux<ProductEntity>

    @Query("SELECT id, name, price, quantity, id_category FROM tb_product WHERE id = $1")
    fun findAllById(ids: List<Long>): Flux<ProductEntity>

    @Modifying
    @Query("SELECT SUM(price) AS price FROM tb_product WHERE id IN(:ids)")
    fun findByIdTotalPrice(ids: List<Long?>): Mono<ProductEntity>

    fun findById(id: Int): Mono<ProductEntity>

    fun deleteById(id: Int): Mono<Void>

    @Query("SELECT sum(price) FROM tb_product WHERE id IN ($1)")
    fun findSumProductById(ids: List<Long?>): Mono<BigDecimal>
}