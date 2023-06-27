package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.client.service.product.ProductServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.repository.order.IOrderRepository
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import com.mvp.delivery.utils.constants.LogMsgConstants
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
import java.math.RoundingMode

@Service
class OrderServiceImpl @Autowired constructor(
    orderRepository: IOrderRepository,
    private val authValidatorService: AuthValidatorService,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl
) : IOrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    @Autowired
    private val orderRepository: IOrderRepository

    init {
        this.orderRepository = orderRepository
    }

    override fun getOrderById(id: Int, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .map { it.toDTO() }
    }

    override fun saveInitialOrders(order: OrderDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.save(order.toEntity())
            .map { it.toDTO() }
    }

    fun test(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        return userService.getByUsername(orderRequestDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)))
            .flatMap { userDTO ->
                authValidatorService.validate(authentication, userDTO)
                    .then(Mono.just(userDTO))
            }.flatMap { userDTO ->
                val listProductId = orderRequestDTO.orderProduct.map { it.idProduct!! }.toList()
                val allProducts = productService.findByIdTotalPrice(listProductId)
                Mono.zip(
                    Mono.just(userDTO),
                    orderRepository.findByUsername(orderRequestDTO.username),
                    allProducts
                ).flatMap { tuple ->
                   if(tuple.t2.id != null) {
                       val saveOrderEntity = OrderEntity(
                            idClient = tuple.t1.id,
                            totalPrice = tuple.t3.price
                       )
                       orderRepository.save(saveOrderEntity)
                    } else {
                        val saveOrderEntity = OrderEntity(
                            idClient = tuple.t1.id,
                            status = "PENDING",
                            totalPrice = tuple.t3.price
                        )
                       orderRepository.save(saveOrderEntity)
                    }
                    Mono.just(tuple.t2)
                }.map {
                    OrderResponseDTO(it.toDTO())
                }
            }
    }

    override fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        return userService.getByUsername(orderRequestDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)))
            .flatMap {
                authValidatorService.validate(authentication, it)
                    .then(Mono.just(it))
            }.flatMap { userDTO ->
                orderRepository.findByUsername(orderRequestDTO.username)
                    .flatMap {orderEntity ->
                        orderEntity.idClient = userDTO.id
                        orderEntity.toMono()
                    }
                    .switchIfEmpty {
                        var total: BigDecimal = BigDecimal.ZERO
                        productService.productsCache.map { it.price }
                            .reduce { acc, next -> total.plus(acc.add(next)) }
                            .map { total.add(it) }
                        return@switchIfEmpty Mono.just(
                            OrderEntity(
                                idClient = userDTO.id,
                                status = "PENDING",
                                totalPrice = total.setScale(8, RoundingMode.HALF_UP)
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
                    orderUpdate.totalPrice = it.totalPrice
                    Mono.just(orderUpdate)
                }.subscribe()
                Mono.just(OrderResponseDTO(orderUpdate.toDTO()))
            }
    }

    override fun updateOrderProduct(id: Int, order: OrderDTO, authentication: Authentication): OrderDTO {
        TODO("Not yet implemented")
    }

    override fun updateOrderStatus(id: Int, orderStatusDTO: OrderStatusDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.valueOf(orderStatusDTO.status).value
            }.onErrorMap { Exceptions.BadStatusException(ErrorMsgConstants.ERROR_STATUS_NOT_FOUND) }
            .flatMap(orderRepository::save)
                .map { return@map it.toDTO() }
    }

    override fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.findById(orderFinishDTO.idOrder.toInt())
            .doOnNext { orderFinished ->
                orderFinished.status = OrderStatusEnum.FINISHED.value
                orderFinished.isFinished = true
            }.onErrorMap { Exceptions.BadStatusException(ErrorMsgConstants.ERROR_STATUS_NOT_FOUND) }
            .flatMap(orderRepository::save)
            .map { return@map it.toDTO() }
    }

    private fun saveAllOrderProduct(data: List<OrderProductEntity>?): Flux<OrderProductEntity> {
        return Flux.fromIterable(data!!).flatMap(orderProductRepository::save)
            .onErrorMap { Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND) }
    }

    override fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductDTO> {
       return orderProductRepository.findAllByIdOrder(id)
           .switchIfEmpty(Flux.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
           .map { it.toDTO() }
    }

    override fun deleteOrderById(id: Int, authentication: Authentication): Mono<Void> {
        return orderRepository.findById(id).flatMap {order ->
            orderProductRepository.deleteByIdOrder(order.id!!)
                .then(orderRepository.deleteById(id))
        }
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO, authentication: Authentication): Mono<Void> {
        val listProductId = products.productId.map { it }.toList()
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
    }

    private fun updateCreateOrder(orderRequestDTO: OrderRequestDTO): Mono<OrderEntity> {
        return orderRepository.findByUsername(orderRequestDTO.username)
            .flatMap {
                orderRepository.save(it)
            }.map { it }
    }
}