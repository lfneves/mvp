package com.mvp.delivery.delivery.application.controller

import com.mvp.delivery.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.delivery.domain.client.service.item.IProductService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/product")
class ProductController(productService: IProductService) {
    private val productService: IProductService

    init {
        this.productService = productService
    }

    @GetMapping
    fun all(): Flux<ProductEntity> {
        return productService.getProducts()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<ProductEntity?>
        get() = productService.getProducts()
            .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/{id}")
    fun getItemById(@PathVariable id: Int): Mono<ProductEntity> {
        return productService.getProductById(id)
            .defaultIfEmpty(ProductEntity())
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateItem(@PathVariable id: Int, @RequestBody productEntity: ProductEntity): Mono<ProductEntity> {
        return productService.updateProduct(id, productEntity)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItem(@PathVariable id: Int): Mono<Void> {
        return productService.deleteProductById(id)
    }

    @PostMapping("/get-by-category")
    @ResponseStatus(HttpStatus.OK)
    fun findByCategory(@RequestBody productEntity: ProductEntity): Flux<ProductEntity> {
        return productService.getByCategory(productEntity.name)
    }
}