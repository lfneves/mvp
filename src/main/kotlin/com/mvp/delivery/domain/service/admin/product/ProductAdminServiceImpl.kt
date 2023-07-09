package com.mvp.delivery.domain.service.admin.product


import com.mvp.delivery.domain.model.product.CategoryDTO
import com.mvp.delivery.domain.model.product.ProductDTO
import com.mvp.delivery.domain.model.product.ProductRequestDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.product.CategoryRepository
import com.mvp.delivery.infrastruture.repository.product.ProductRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
@CacheConfig(cacheNames = ["productsCache"])
class ProductAdminServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
): ProductAdminService {

    lateinit var productsCache: Flux<ProductDTO>

    init {
        this.productsCache = getProducts()
    }

    @CachePut(cacheNames = ["productsCache"])
    override fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO> {
       return getCategoryByName(productDTO.category.name)
           .switchIfEmpty(
               categoryRepository.findById(productDTO.idCategory)
                   .map { it?.toDTO()}
           ).switchIfEmpty(
                Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_CATEGORY_NOT_FOUD))
            ).flatMap{ category ->
               productDTO.idCategory = category?.id!!
                productRepository.save(productDTO.toEntity())
                    .map { it.toDTO(category) }
            }
    }

    @CachePut(cacheNames = ["productsCache"])
    override fun updateProduct(id: Int, productRequestDTO: ProductRequestDTO): Mono<ProductDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
            .flatMap { product ->
                product.updateUserEntity(product, productRequestDTO.toEntity())
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
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
            .then(
                productRepository.deleteById(id)
            )
    }

    @CachePut("productsCache")
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

    override fun getAllCategory(): Flux<CategoryDTO> {
        return categoryRepository
            .findAll()
            .map { it?.toDTO() }
    }

    override fun deleteAllProducts(): Mono<Void> {
         return productRepository
             .deleteAll()
    }

    private fun getCategoryByName(name: String): Mono<CategoryDTO> {
        return categoryRepository.findByName(name)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
            .map { it.toDTO() }
            .toMono()

    }
}