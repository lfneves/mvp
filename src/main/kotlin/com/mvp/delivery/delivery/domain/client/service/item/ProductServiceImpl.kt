package com.mvp.delivery.delivery.domain.client.service.item

import com.mvp.delivery.delivery.domain.exception.Exceptions
import com.mvp.delivery.delivery.infrastruture.entity.product.ProductEntity
import com.mvp.delivery.delivery.infrastruture.repository.product.ICategoryRepository
import com.mvp.delivery.delivery.infrastruture.repository.product.IProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    override fun saveProduct(productEntity: ProductEntity): Mono<ProductEntity> {
        return productRepository.save(productEntity).doOnSubscribe { return@doOnSubscribe }
    }

    override fun updateProduct(id: Int, productEntity: ProductEntity): Mono<ProductEntity> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Item not found")))
            .flatMap { ItemFlat ->
                ItemFlat.id = productEntity.id
                saveProduct(ItemFlat)
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

    override fun getProducts(): Flux<ProductEntity> {
        return productRepository
            .findAll()
            .map{ it }
    }

    override fun deleteAllProducts(): Mono<Void> {
         return productRepository
             .deleteAll()
             .block().
             toMono()
    }

    override fun getByCategory(name: String): Flux<ProductEntity> {
        return categoryRepository.findByName(name)
            .flatMap {category ->
                productRepository.findByIdCategory(category.id)
                    .map { product ->
                        product.copy(categoryEntity = category)
                    }
            }
    }
}