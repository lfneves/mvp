package com.mvp.delivery.domain.admin.product


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

@Service
@CacheConfig(cacheNames = ["products"])
class ProductAdminServiceImpl(
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository
) : ProductAdminService {
    @Autowired
    private val productRepository: ProductRepository
    private val categoryRepository: CategoryRepository

    lateinit var productsCache: Flux<ProductDTO>

    init {
        this.productRepository = productRepository
        this.categoryRepository = categoryRepository
        this.productsCache = getProducts()
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
}