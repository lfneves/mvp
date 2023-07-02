package com.mvp.delivery.application.controller.v1.admin


import com.mvp.delivery.domain.admin.product.ProductAdminService
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
@RequestMapping("/api/v1/admin/product")
class ProductAdminController(productAdminService: ProductAdminService) {
    private val productAdminService: ProductAdminService

    init {
        this.productAdminService = productAdminService
    }

    @get:GetMapping(path = ["/flux"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    @get:Operation(
        summary = "Perfomace produtos",
        description = "Utilizado para testes de performace e latência alterado o delay entre outros parametros",
        tags = ["Produtos Performace"]
    )
    val flux: Flux<ProductDTO?>
        get() = productAdminService.getProducts()
            .delayElements(Duration.ofSeconds(1)).log()

    @PostMapping("/create")
    @Operation(
        summary = "Cria produtos",
        description = "Cadastro de produtos para utilização visualização de clientes",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productAdminService.saveProduct(productDTO)
    }

    @PutMapping("/update/{id}")
    @Operation(
        summary = "Atualiza produto pelo id",
        description = "Atualiza produto cadastrados é preciso informa a categoria",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateProduct(@PathVariable id: Int, @RequestBody productDTO: ProductDTO): Mono<ProductDTO> {
        return productAdminService.updateProduct(id, productDTO)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove produto pelo id",
        description = "remove produto cadastrado pelo id informado",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Int): Mono<Void> {
        return productAdminService.deleteProductById(id)
    }
}