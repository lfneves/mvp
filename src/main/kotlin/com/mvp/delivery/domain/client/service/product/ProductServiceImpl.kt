package com.mvp.delivery.domain.client.service.product


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.product.CategoryRepository
import com.mvp.delivery.infrastruture.repository.product.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal

@Service
class ProductServiceImpl(
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository
) : ProductService {
    @Autowired
    private val productRepository: ProductRepository
    private val categoryRepository: CategoryRepository

    lateinit var productsCache: Flux<ProductDTO>

    init {
        this.productRepository = productRepository
        this.categoryRepository = categoryRepository
        this.productsCache = getProducts()
    }

    override fun getProductById(id: Int): Mono<ProductDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Item not found")))
            .map { it.toDTO() }
    }

    @Cacheable("products")
    override fun getProducts(): Flux<ProductDTO> {
        return productRepository
            .findAll()
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                    return@map product.toDTO(category?.toDTO())
                }
            }
    }

    override fun getAllById(id: List<Long?>): Flux<ProductDTO> {
        return productRepository
            .findAllById(id)
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                        return@map product.toDTO(category?.toDTO())
                    }
            }
    }

    override fun getByIdTotalPrice(ids: List<Long?>): Mono<BigDecimal> {
        return productRepository.findByIdTotalPrice(ids)
            .flatMap { Mono.just(it.price) }
            .map { it }
    }

    private fun getCategoryByName(name: String): Mono<CategoryDTO> {
        return categoryRepository.findByName(name)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Product category not found")))
            .map { it.toDTO() }
            .toMono()

    }

    override fun getProductsByCategoryByName(name: String): Flux<ProductDTO> {
        return categoryRepository.findByName(name)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Product category not found")))
            .flatMap {category ->
                productRepository.findByIdCategory(category.id)
                    .map { product ->
                        product.toDTO(category.toDTO())
                    }
            }
    }
}