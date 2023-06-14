package com.mvp.delivery.delivery.service.item

import com.mvp.delivery.delivery.exception.NotFoundException
import com.mvp.delivery.delivery.model.Product
import com.mvp.delivery.delivery.repository.item.ICategoryRepository
import com.mvp.delivery.delivery.repository.item.IProductRepository
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

    override fun getProductById(id: Int): Mono<Product> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Item not found")))
    }

    override fun saveProduct(Product: Product): Mono<Product> {
        return productRepository.save(Product).doOnSubscribe { return@doOnSubscribe }
    }

    override fun updateProduct(id: Int, Product: Product): Mono<Product> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Item not found")))
            .flatMap { ItemFlat ->
                ItemFlat.id = Product.id
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

    override fun getProducts(): Flux<Product> {
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

    override fun getByCategory(name: String): Flux<Product> {
        return categoryRepository.findByName(name)
            .flatMap {category ->
                productRepository.findByIdCategory(category.id)
                    .map { product ->
                        product.copy(category = category)
                    }
            }
    }
}