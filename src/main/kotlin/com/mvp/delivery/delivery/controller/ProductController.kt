package com.mvp.delivery.delivery.controller

import com.mvp.delivery.delivery.model.Product
import com.mvp.delivery.delivery.service.item.IProductService
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
    fun all(): Flux<Product> {
        return productService.getProducts()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<Product?>
        get() = productService.getProducts()
            .delayElements(Duration.ofSeconds(1)).log()

    @GetMapping("/{id}")
    fun getItemById(@PathVariable id: Int): Mono<Product> {
        return productService.getProductById(id)
            .defaultIfEmpty(Product())
    }

    @PutMapping("/update-product/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateItem(@PathVariable id: Int, @RequestBody product: Product): Mono<Product> {
        return productService.updateProduct(id, product)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItem(@PathVariable id: Int): Mono<Void> {
        return productService.deleteProductById(id)
    }

    @PostMapping("/get-by-category")
    @ResponseStatus(HttpStatus.OK)
    fun findByCategory(@RequestBody product: Product): Flux<Product> {
        return productService.getByCategory(product.name)
    }
}