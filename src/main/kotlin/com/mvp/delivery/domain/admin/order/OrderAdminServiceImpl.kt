package com.mvp.delivery.domain.admin.order

import com.mvp.delivery.domain.client.model.order.*
import com.mvp.delivery.domain.client.model.order.enums.OrderStatusEnum
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.order.OrderRepository
import com.mvp.delivery.utils.constants.ErrorMsgConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderAdminServiceImpl @Autowired constructor(
    orderRepository: OrderRepository
) : OrderAdminService {
    var logger: Logger = LoggerFactory.getLogger(OrderAdminServiceImpl::class.java)

    @Autowired
    private val orderRepository: OrderRepository

    init {
        this.orderRepository = orderRepository
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

    override fun getOrders(): Flux<OrderDTO> {
        return orderRepository
            .findAllOrder()
            .map{ it?.toDTO() }
    }

    override fun deleteAllOrders(): Mono<Void> {
         return orderRepository
             .deleteAll()
    }
}