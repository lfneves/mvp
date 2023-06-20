package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.repository.order.IOrderRepository
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

    override fun getOrderById(id: Int): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
            .map { it.toDTO() }
    }

    override fun saveInitialOrder(orderDTO: OrderDTO): Mono<OrderDTO> {
        return orderRepository.save(orderDTO.toEntity())
            .map { it.toDTO() }
    }

    override fun saveOrder(orderDTO: OrderDTO): Mono<OrderDTO> {
        return orderRepository.save(orderDTO.toEntity())
            .map { it.toDTO() }
    }

    override fun updateOrder(id: Int, orderDTO: OrderDTO): Mono<OrderDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException("Order not found")))
            .flatMap { orderFlat ->
                orderFlat.id = orderDTO.id
                saveOrder(orderFlat.toDTO())
            }
    }

    override fun deleteOrderById(id: Int): Mono<Void> {
        // delete Order
        return orderRepository.findById(id).flatMap { Order ->
            orderRepository.deleteById(Order.id!!).then(orderRepository.deleteById(id))
        }
    }

    override fun getOrders(): Flux<OrderDTO> {
        return orderRepository
            .findAll()
            .map{ it?.toDTO() }
    }

    override fun deleteAllOrders(): Mono<Void> {
         return orderRepository
             .deleteAll()
             .block().
             toMono()
    }
}