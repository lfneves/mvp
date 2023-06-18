package com.mvp.delivery.delivery.domain.client.service.order

import com.mvp.delivery.delivery.domain.exception.Exceptions
import com.mvp.delivery.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.delivery.infrastruture.repository.order.IOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class OrderServiceImpl(
    orderRepository: IOrderRepository,
) : IOrderService {
    @Autowired
    private val orderRepository: IOrderRepository

    init {
        this.orderRepository = orderRepository
    }

    override fun getOrderById(id: Int): Mono<OrderEntity> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
    }

    override fun saveInitialOrder(orderEntity: OrderEntity): Mono<OrderEntity> {
        return orderRepository.save(orderEntity).block().toMono()
    }

    override fun saveOrder(orderEntity: OrderEntity): Mono<OrderEntity> {
        return orderRepository.save(orderEntity).doOnSubscribe { return@doOnSubscribe }
    }

    override fun updateOrder(id: Int, orderEntity: OrderEntity): Mono<OrderEntity> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
            .flatMap { OrderFlat ->
                OrderFlat.id = orderEntity.id
                saveOrder(OrderFlat)
            }
    }

    override fun deleteOrderById(id: Int): Mono<Void> {
        // delete Order
        return orderRepository.findById(id).flatMap { Order ->
            orderRepository.deleteById(Order.id!!).then(orderRepository.deleteById(id))
        }
    }

    override fun getOrders(): Flux<OrderEntity> {
        return orderRepository
            .findAll()
            .map{ it }
    }

    override fun deleteAllOrders(): Mono<Void> {
         return orderRepository
             .deleteAll()
             .block().
             toMono()
    }
}