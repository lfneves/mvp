package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.client.service.product.IProductService
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
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
    fun all(): Flux<ProductDTO> {
        return productService.getProducts()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    val flux: Flux<ProductDTO?>
        get() = productService.getProducts()
            .delayElements(Duration.ofSeconds(1)).log()

    @DeleteMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productService.saveProduct(productDTO)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Int): Mono<ProductEntity> {
        return productService.getProductById(id)
            .defaultIfEmpty(ProductEntity())
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateProduct(@PathVariable id: Int, @RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productService.updateProduct(id, productDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Int): Mono<Void> {
        return productService.deleteProductById(id)
    }

    @PostMapping("/get-by-category")
    @ResponseStatus(HttpStatus.OK)
    fun findByCategory(@RequestBody category: CategoryDTO): Flux<ProductDTO> {
        return productService.getProductsByCategoryByName(category.name)
    }
}