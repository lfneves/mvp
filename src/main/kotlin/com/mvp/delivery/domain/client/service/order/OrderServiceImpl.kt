package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.auth.AuthenticationVO
import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.client.model.product.ProductDTO
import com.mvp.delivery.domain.client.model.product.ProductRemoveOrderDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.client.service.product.ProductServiceImpl
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import com.mvp.delivery.infrastruture.repository.order.OrderRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val authValidatorService: AuthValidatorService,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl
): OrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    override fun getOrderById(id: Int, authentication: Authentication): Mono<OrderByIdResponseDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .map { it.toResponseDTO() }
    }

    override fun saveInitialOrders(order: OrderDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.save(order.toEntity())
            .map { it.toDTO() }
    }

    override fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        authentication as AuthenticationVO

        var total: BigDecimal = BigDecimal.ZERO
        var listIDproduct = mutableListOf<Long>()
        orderRequestDTO.orderProduct.forEach {
            it.idProduct?.let { it1 -> listIDproduct.add(it1) }
        }

        return userService.getByUsername(authentication.iAuthDTO.username!!)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)))
            .flatMap {
                productService.getAllById(listIDproduct).toFlux()
                    .collectList()
                    .flatMap { product ->
                        if(product.isEmpty()) {
                            Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND))
                        } else {
                            total = product.reduce { acc, productDTO -> ProductDTO(price = acc.price.add(productDTO.price)) }.price
                            Mono.just(it)
                        }
                    }
            }.switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
            .flatMap {
                authValidatorService.validate(authentication, it)
                    .then(Mono.just(it))
            }.flatMap { userDTO ->
                orderRepository.findByUsername(authentication.iAuthDTO.username!!)
                    .flatMap {orderEntity ->
                        orderEntity.idClient = userDTO.id
                        orderEntity.totalPrice.add(total)
                        orderEntity.toMono()
                    }.switchIfEmpty {
                        return@switchIfEmpty Mono.just(
                            OrderEntity(
                                idClient = userDTO.id,
                                status = OrderStatusEnum.PENDING.value,
                                totalPrice = total
                            )
                        ).flatMap {
                            orderRepository.save(it)
                        }
                    }
            }.map {orderEntity ->
                orderRequestDTO.orderProduct.forEach {
                    it.idOrder = orderEntity.id
                }
                saveAllOrderProduct(orderRequestDTO.toEntityList().toList()).subscribe()
                OrderResponseDTO(orderEntity.toDTO())
            }
    }

    override fun updateOrderProduct(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Mono<OrderResponseDTO> {
        authentication as AuthenticationVO
        return orderRepository.findByUsername(authentication.iAuthDTO.username!!)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap {
                logger.info(it.status + " " + OrderStatusEnum.PAID.value)
                if(it.status == OrderStatusEnum.PAID.value) {
                    return@flatMap Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PAID_CONSTANT))
                }
                return@flatMap createOrder(orderRequestDTO, authentication)
            }
    }

    private fun saveAllOrderProduct(data: List<OrderProductEntity>?): Flux<OrderProductEntity> {
        return Flux.fromIterable(data!!).flatMap(orderProductRepository::save)
    }

    override fun getAllOrderProductsByIdOrder(id: Long, authentication: Authentication): Flux<OrderProductResponseDTO> {
       return orderProductRepository.findAllByIdOrderInfo(id)
           .switchIfEmpty(Flux.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)))
           .map { it.toDTO() }
    }

    override fun deleteOrderById(id: Int, authentication: Authentication): Mono<Void> {
        return orderRepository.findById(id).flatMap {order ->
            orderProductRepository.deleteByIdOrder(order.id!!)
                .then(orderRepository.deleteById(id))
        }
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO, authentication: Authentication): Mono<Void> {
        authentication as AuthenticationVO
        val listProductId = products.orderProductId.map { it }.toList()
        return orderRepository.findByUsername(authentication.iAuthDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap { orderProductRepository.deleteById(listProductId) }
    }

    override fun checkoutOrder(orderCheckoutDTO: OrderCheckoutDTO, authentication: Authentication): Mono<Boolean> {
        authentication as AuthenticationVO
        return orderRepository.findByUsername(authentication.iAuthDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.PAID.value
                val randomMinutes = (20..75).random().toLong()
                val z = ZoneId.of( "America/Sao_Paulo")
                setStatus.waitingTime = ZonedDateTime.now(z).plusMinutes(randomMinutes).toLocalDateTime()
            }.flatMap(orderRepository::save)
            .thenReturn(true)
    }
}