package com.mvp.delivery.domain.client.service.product


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.client.model.product.ProductTotalPriceDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.infrastruture.repository.product.ICategoryRepository
import com.mvp.delivery.infrastruture.repository.product.IProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal

@Service
@CacheConfig(cacheNames = ["products"])
class ProductServiceImpl(
    productRepository: IProductRepository,
    categoryRepository: ICategoryRepository
) : IProductService {
    @Autowired
    private val productRepository: IProductRepository
    private val categoryRepository: ICategoryRepository

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

    @CacheEvict(cacheNames = ["products"], allEntries = true)
    override fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO> {
       return getCategoryByName(productDTO.category.name)
           .switchIfEmpty(
               categoryRepository.findById(productDTO.idCategory)
                   .map { it?.toDTO()}
           ).switchIfEmpty(
                Mono.error(Exceptions.NotFoundException("Category not found"))
            ).flatMap{ category ->
               productDTO.idCategory = category?.id!!
                productRepository.save(productDTO.toEntity())
                    .map { it.toDTO(category) }
            }
    }

    //TODO fix save category
    @CacheEvict(cacheNames = ["products"], allEntries = true)
    override fun updateProduct(id: Int, productDTO: ProductDTO): Mono<ProductDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Product not found")))
            .flatMap { product ->
                product.updateUserEntity(product, productDTO.toEntity())
                productRepository.save(product)
                    .flatMap { productEntity ->
                        productEntity.idCategory?.let {
                            categoryRepository.findById(it)
                                .flatMap {categoryEntity ->
                                    Mono.justOrEmpty(productEntity.toDTO(categoryEntity?.toDTO()))
                                }
                        }
                    }
            }
    }

    override fun deleteProductById(id: Int): Mono<Void> {
        // delete Item
        return productRepository.findById(id)
            .flatMap { product ->
            productRepository.deleteById(product.id!!)
                .then(categoryRepository.deleteById(id))
        }
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

    override fun deleteAllProducts(): Mono<Void> {
         return productRepository
             .deleteAll()
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

    fun findByIdTotalPrice(ids: List<Long>): Mono<ProductDTO> {
        return productRepository.findByIdTotalPrice(ids)
            .map { it.toDTO() }
    }

}