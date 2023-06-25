package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.client.service.product.ProductServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.configuration.CoroutineConfiguration
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.repository.order.IOrderRepository
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import com.mvp.delivery.infrastruture.repository.product.IProductRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal

@Service
class OrderServiceImpl @Autowired constructor(
    orderRepository: IOrderRepository,
    private val authValidatorService: AuthValidatorService,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl,
    private val productRepository: IProductRepository
) : IOrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    @Autowired
    private val orderRepository: IOrderRepository

    init {
        this.orderRepository = orderRepository
    }

    override fun getOrderById(id: Int): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
            .map { it.toDTO() }
    }

    override fun saveInitialOrder(orderDTO: OrderDTO): Mono<OrderDTO> {
        return orderRepository.save(orderDTO.toEntity())
            .map { it.toDTO() }
    }

    override fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        return userService.getByUsername(orderRequestDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("User not found")))
            .flatMap { userDTO ->
                orderRepository.findByUsername(orderRequestDTO.username)
                    .flatMap {orderEntity ->
                        orderEntity.idClient = userDTO.id
                        orderEntity.toMono()
                    }.switchIfEmpty {
                        var total = BigDecimal.ZERO
                        productService.productsCache.map { it.price }
                            .reduce { acc, next -> acc.add(next) }
                            .map { total.add(it) }
                        return@switchIfEmpty Mono.just(
                            OrderEntity(
                                idClient = userDTO.id,
                                status = "PENDING",
                                totalPrice = total
                            )
                        ).flatMap {
                            orderRepository.save(it)
                        }
                    }
            }.map { orderEntity ->
                orderRequestDTO.orderProduct.forEach {
                    it.idOrder = orderEntity.id
                }
                saveAllOrderProduct(orderRequestDTO.toEntityList().toList()).subscribe()
                orderEntity
            }.flatMap { orderUpdate ->
                updateCreateOrder(orderRequestDTO).flatMap {
                    orderUpdate.copy(totalPrice = it.totalPrice)
                    Mono.just(orderUpdate)
                }.subscribe()
                Mono.just(OrderResponseDTO(orderUpdate.toDTO()))
            }
        }

    override fun updateOrderProduct(id: Int, order: OrderDTO): OrderDTO {
        TODO("Not yet implemented")
    }

    override fun updateOrderStatus(id: Int, orderStatusDTO: OrderStatusDTO): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.valueOf(orderStatusDTO.status).value
            }.onErrorMap { Exceptions.BadStatusException("Status não encontrado utilize: PENDING, PREPARING, PAID, FINISHED") }
            .flatMap(orderRepository::save)
                .map { return@map it.toDTO() }
    }

    fun saveAllOrderProduct(data: List<OrderProductEntity>?): Flux<OrderProductEntity> {
        return Flux.fromIterable(data!!).flatMap(orderProductRepository::save)
            .onErrorMap { Exceptions.NotFoundException("Produto não encontrado.") }
    }

    override fun getAllOrderProductsByIdOrder(id: Long): Flux<OrderProductDTO> {
       return orderProductRepository.findAllByIdOrder(id)
           .switchIfEmpty(Flux.error(Exceptions.NotFoundException("Não foram encontrados produtos.")))
           .map { it.toDTO() }
    }

    suspend fun findAllProductByIdOrder(id: Long): Flux<OrderProductDTO> =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderProductRepository.findAllByIdOrder(id).flatMap {
                    Flux.just(
                        OrderProductDTO(
                        id = it.id,
                        idProduct = it.idProduct,
                        idOrder = it.idOrder
                    )
                )
            }
        }


    private fun updateCreateOrder(orderRequestDTO: OrderRequestDTO): Mono<OrderEntity> {
        return orderRepository.findByUsername(orderRequestDTO.username)
            .flatMap {
                orderRepository.save(it)
            }.map { it }
    }

    suspend fun saveNewOrder(orderEntity: OrderEntity): OrderDTO? =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderRepository.save(orderEntity)
        }.awaitSingle()
            .toDTO()

    override fun deleteOrderById(id: Int): Mono<Void> {
        return orderRepository.findById(id).flatMap {order ->
            orderProductRepository.deleteByIdOrder(order.id!!)
                .then(orderRepository.deleteById(id))
        }
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO): Mono<Void> {
        var listProductId = products.productId.map { it }.toList()
        return orderProductRepository.deleteByIdProduct(listProductId)
    }

    override fun getOrders(): Flux<OrderDTO> {
        return orderRepository
            .findAllOrder()
            .map{ it?.toDTO() }
    }

    override fun deleteAllOrders(): Mono<Void> {
         return orderRepository
             .deleteAll()
             .block().
             toMono()
    }
}