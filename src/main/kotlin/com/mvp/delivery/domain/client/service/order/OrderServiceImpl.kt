package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.client.service.product.ProductServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.repository.order.OrderRepository
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
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
    orderRepository: OrderRepository,
    private val authValidatorService: AuthValidatorService,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl
) : OrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    @Autowired
    private val orderRepository: OrderRepository

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

    override fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        authentication as AuthenticationVO
        return userService.getByUsername(authentication.iAuthDTO.username!!)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)))
            .flatMap {
                authValidatorService.validate(authentication, it)
                    .then(Mono.just(it))
            }.flatMap { userDTO ->
                orderRepository.findByUsername(authentication.iAuthDTO.username!!)
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
                                status = OrderStatusEnum.PENDING.value,
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
                updateCreateOrder(authentication.iAuthDTO.username!!).flatMap {
                    orderUpdate.totalPrice = it.totalPrice
                    Mono.just(orderUpdate)
                }.subscribe()
                Mono.just(OrderResponseDTO(orderUpdate.toDTO()))
            }
    }

    override fun updateOrderProduct(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        authentication as AuthenticationVO
        return orderRepository.findByUsername(authentication.iAuthDTO.username!!)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap {
                return@flatMap createOrder(orderRequestDTO, authentication)
            }
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

    override fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductResponseDTO> {
       return orderProductRepository.findAllByIdOrderInfo(id)
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
        val listProductId = products.orderProductId.map { it }.toList()
        return orderProductRepository.deleteById(listProductId)
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

    private fun updateCreateOrder(username: String): Mono<OrderEntity> {
        return orderRepository.findByUsername(username)
            .flatMap {
                orderRepository.save(it)
            }.map { it }
    }

    override fun checkoutOrder(orderCheckoutDTO: OrderCheckoutDTO, authentication: Authentication): Mono<Void> {
        authentication as AuthenticationVO
        return orderRepository.findByUsername(authentication.iAuthDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.PAID.value
            }.onErrorMap { Exceptions.BadStatusException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND) }
            .flatMap(orderRepository::save)
            .then()
            //.map { return@map it.toDTO() }
    }
}