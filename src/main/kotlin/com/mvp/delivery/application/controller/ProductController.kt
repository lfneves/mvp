package com.mvp.delivery.application.controller


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.client.service.product.ProductService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/product")
class ProductController(productService: ProductService) {
    private val productService: ProductService

    init {
        this.productService = productService
    }

    @GetMapping
    @Operation(
        summary = "Busca todos produtos",
        description = "Busca todos produtos cadastrados",
        tags = ["Produtos Administrador"]
    )
    fun all(): Flux<ProductDTO> {
        return productService.getProducts()
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace produtos",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Produtos Performace"]
    )
    val flux: Flux<ProductDTO?>
        get() = productService.getProducts()
            .delayElements(Duration.ofSeconds(1)).log()

    @PostMapping("/create")
    @Operation(
        summary = "Cria produtos",
        description = "Cadastro de produtos para utilização visualização de clientes",
        tags = ["Produtos Administrador"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productService.saveProduct(productDTO)
    }

    @PutMapping("/update/{id}")
    @Operation(
        summary = "Atualiza produto pelo id",
        description = "Atualiza produto cadastrados é preciso informa a categoria",
        tags = ["Produtos Administrador"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateProduct(@PathVariable id: Int, @RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productService.updateProduct(id, productDTO)
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

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove produto pelo id",
        description = "remove produto cadastrado pelo id informado",
        tags = ["Produtos"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Int): Mono<Void> {
        return productService.deleteProductById(id)
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