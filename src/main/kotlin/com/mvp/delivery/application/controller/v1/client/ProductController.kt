package com.mvp.delivery.application.controller.v1.client


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.client.service.product.ProductService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/product")
class ProductController(private val productService: ProductService) {

    @GetMapping
    @Operation(
        summary = "Busca todos produtos",
        description = "Busca todos produtos cadastrados",
        tags = ["Produtos"]
    )
    fun all(): Flux<ProductDTO> {
        return productService.getProducts()
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca produtos pelo id",
        description = "Busca produtos cadastrados pelo id informado",
        tags = ["Produtos"]
    )
    fun getProductById(@PathVariable id: Int): Mono<ProductDTO> {
        return productService.getProductById(id)
            .defaultIfEmpty(ProductDTO())
    }

    @GetMapping("/get-by-category-name")
    @Operation(
        summary = "Busca produtos por categoria",
        description = "Busca produtos pelo nome da categoria informado",
        tags = ["Produtos"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun findByCategory(@RequestBody category: CategoryDTO): Flux<ProductDTO> {
        return productService.getProductsByCategoryByName(category.name)
    }
}