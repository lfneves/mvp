package com.mvp.delivery.application.controller.v1.admin


import com.mvp.delivery.domain.service.admin.product.ProductAdminService
import com.mvp.delivery.domain.model.product.CategoryDTO
import com.mvp.delivery.domain.model.product.ProductDTO
import com.mvp.delivery.domain.model.product.ProductRequestDTO
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/admin/product")
class ProductAdminController(private val productAdminService: ProductAdminService) {

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
    fun createProduct(@RequestBody productDTO: ProductDTO): ResponseEntity<Mono<ProductDTO>> {
        return ResponseEntity.ok(productAdminService.saveProduct(productDTO))
    }

    @PutMapping("/update/{id}")
    @Operation(
        summary = "Atualiza produto pelo id",
        description = "Atualiza produto cadastrados é preciso informa a categoria",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateProduct(@PathVariable id: Int, @RequestBody productRequestDTO: ProductRequestDTO): ResponseEntity<Mono<ProductDTO>> {
        return ResponseEntity.ok(productAdminService.updateProduct(id, productRequestDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove produto pelo id",
        description = "remove produto cadastrado pelo id informado",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Int): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(productAdminService.deleteProductById(id))
    }

    @GetMapping("/get-all-category")
    @Operation(
        summary = "Busca todas categorias",
        description = "Busca todas categorias cadastradas",
        tags = ["Produtos"]
    )
    fun getAllCategory(): ResponseEntity<Flux<CategoryDTO>> {
        return ResponseEntity.ok(productAdminService.getAllCategory())
    }
}