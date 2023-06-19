package com.mvp.delivery.domain.client.service.product


import com.mvp.delivery.domain.client.model.product.CategoryDTO
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.infrastruture.repository.product.ICategoryRepository
import com.mvp.delivery.infrastruture.repository.product.IProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Service
class ProductServiceImpl(
    productRepository: IProductRepository,
    categoryRepository: ICategoryRepository,
) : IProductService {
    @Autowired
    private val productRepository: IProductRepository
    private val categoryRepository: ICategoryRepository

    init {
        this.productRepository = productRepository
        this.categoryRepository = categoryRepository
    }

    override fun getProductById(id: Int): Mono<ProductEntity> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Item not found")))
    }

    override fun saveProduct(productDTO: ProductDTO): Mono<ProductDTO> {
       return getCategoryByName(productDTO.category.name)
            .switchIfEmpty {
                categoryRepository.findById(productDTO.category.toEntity().id!!)
                    .map { it?.toDTO() ?: CategoryDTO() }
            }.switchIfEmpty(
                Mono.error(Exceptions.NotFoundException("Category not found"))
            ).flatMap{ category ->
                productRepository.save(productDTO.toEntity())
                    .map { it.toDTO(category) }
            }
    }

    override fun updateProduct(id: Int, productDTO: ProductDTO): Mono<ProductDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Product not found")))
            .flatMap { product ->
                product.id = productDTO.id
                saveProduct(product.toDTO())
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

    override fun getProducts(): Flux<ProductDTO> {
        return productRepository
            .findAll()
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                    return@map product.toDTO(product, category!!)
                }
            }
    }

    override fun deleteAllProducts(): Mono<Void> {
         return productRepository
             .deleteAll()
             .block().
             toMono()
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
                        product.toDTO(product, category)
                    }
            }
    }
}