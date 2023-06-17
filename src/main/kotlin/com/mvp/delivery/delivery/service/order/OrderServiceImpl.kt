package com.mvp.delivery.delivery.service.order

import com.mvp.delivery.delivery.exception.Exceptions
import com.mvp.delivery.delivery.model.Order
import com.mvp.delivery.delivery.repository.order.IOrderRepository
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

    override fun getOrderById(id: Int): Mono<Order> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
    }

    override fun saveInitialOrder(order: Order): Mono<Order> {
        return orderRepository.save(order).block().toMono()
    }

    override fun saveOrder(order: Order): Mono<Order> {
        return orderRepository.save(order).doOnSubscribe { return@doOnSubscribe }
    }

    override fun updateOrder(id: Int, Order: Order): Mono<Order> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
            .flatMap { OrderFlat ->
                OrderFlat.id = Order.id
                saveOrder(OrderFlat)
            }
    }

    override fun deleteOrderById(id: Int): Mono<Void> {
        // delete Order
        return orderRepository.findById(id).flatMap { Order ->
            orderRepository.deleteById(Order.id!!).then(orderRepository.deleteById(id))
        }
    }

    override fun getOrders(): Flux<Order> {
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