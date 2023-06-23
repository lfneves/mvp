package com.mvp.delivery.domain.client.service.order

import com.mvp.delivery.domain.client.model.order.OrderDTO
import com.mvp.delivery.domain.client.model.order.OrderProductDTO
import com.mvp.delivery.domain.client.model.order.OrderRequestDTO
import com.mvp.delivery.domain.client.model.order.OrderResponseDTO
import com.mvp.delivery.domain.client.service.auth.validator.AuthValidatorService
import com.mvp.delivery.domain.client.service.user.UserServiceImpl
import com.mvp.delivery.domain.configuration.CoroutineConfiguration
import com.mvp.delivery.domain.exception.Exceptions
import com.mvp.delivery.infrastruture.entity.order.OrderEntity
import com.mvp.delivery.infrastruture.entity.order.OrderProductEntity
import com.mvp.delivery.infrastruture.repository.order.IOrderRepository
import com.mvp.delivery.infrastruture.repository.order.OrderProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class OrderServiceImpl @Autowired constructor(
    orderRepository: IOrderRepository,
    private val authValidatorService: AuthValidatorService,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
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

    override fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): Flux<OrderResponseDTO> {
        userService.getByUsername(orderRequestDTO.username)
            .flatMap { userDTO ->
                orderRepository.existsByIdClient(userDTO.id!!)
                    .flatMap {orderExists ->
                        if(!orderExists) {
                            orderRepository.save(OrderEntity(idClient = userDTO.id)).subscribe()
                            orderRepository.findByUsername(orderRequestDTO.username)
                                .flatMap { orderEntity ->
                                    Mono.just(orderRequestDTO.orderProduct.forEach {
                                        it.copy(idOrder = orderEntity.id)
                                    })
                                }
                        }
                        Mono.empty<OrderResponseDTO>()
                    }
            }.subscribe()

        saveAllOrderProduct(orderRequestDTO.toEntityList().toList()).subscribe()

        return orderRepository.findByUsername(orderRequestDTO.username)
            .flatMap {orderEntity ->
                orderProductRepository.findAllByIdOrder(orderEntity.id!!)
                    .flatMap {orderProduct ->
                        Flux.just(OrderResponseDTO(orderDTO = orderEntity.toDTO(), orderProduct = Flux.just(orderProduct.toDTO())))
                    }
            }
    }

    fun saveAllOrderProduct(data: List<OrderProductEntity>?): Flux<OrderProductEntity?> {
        return Flux.fromIterable(data!!).flatMap<OrderProductEntity?>(orderProductRepository::save)
    }

//    override suspend fun createOrder(orderRequestDTO: OrderRequestDTO, authentication: Authentication): OrderResponseDTO {
//        authValidatorService.validadeByUsername(orderRequestDTO.username)
//
//        var responseOrder = OrderResponseDTO()
//        val userDTO = withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
//            userService.getByUsername(orderRequestDTO.username)
//        }.awaitSingle()
//
//        val orderExists =  withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
//            orderRepository.existsByIdClient(userDTO.id!!).map { it }
//        }.awaitSingle()
//
////        if(!orderExists) {
//            val orderEntity = OrderEntity(idClient = userDTO.id!!)
//            val newOrder = saveNewOrder(orderEntity)
//            val listOrderProductEntity = mutableListOf<OrderProductEntity>()
//            orderRequestDTO.orderProduct.forEach{
//                it.idOrder = newOrder?.id
//                listOrderProductEntity.add(it.toEntity())
//            }
//            responseOrder.orderDTO = newOrder
//            orderProductRepository.saveAll(listOrderProductEntity).subscribe()
//
////            responseOrder.orderProduct =
////            responseOrder.orderProduct = responseOrder.orderDTO?.id?.let { it ->
////                orderProductRepository.findAllByIdOrder(it)
////                    .map { orderProductEntity -> orderProductEntity.toDTO() }
////            }
////        } else {
////            val listProduct = orderRepository.findByUsername(orderRequestDTO.username)
////            println(listProduct)
////        }
//        return responseOrder
//    }

    suspend fun findAllProductByIdOrder(id: Long): Flux<OrderProductDTO> =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderProductRepository.findAllByIdOrder(id).flatMap {
                Flux.just(OrderProductDTO(
                    id = it.id,
                    idProduct = it.idProduct,
                    idOrder = it.idOrder
                )
                )
            }
        }


    override suspend fun updateOrder(id: Int, order: OrderDTO): OrderDTO {
        TODO("Not yet implemented")
    }

    suspend fun saveNewOrder(orderEntity: OrderEntity): OrderDTO? =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderRepository.save(orderEntity)
        }.awaitSingle()
            .toDTO()

    suspend fun saveOrderProduct(orderProductDTO: OrderProductDTO): OrderProductDTO? =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderProductRepository.save(orderProductDTO.toEntity())
        }.awaitSingle().toDTO()

    suspend fun findOrderUser(orderDTO: OrderDTO): OrderDTO? =
        withContext(CoroutineConfiguration.ioCoroutineScope.coroutineContext) {
            orderDTO.idClient?.let {
                orderRepository.findOrderByIdUserAndStatusNotNull(it)
                    .map { it.toDTO() }
            }
        }?.awaitSingle()

//    override suspend fun updateOrder(id: Int, orderDTO: OrderDTO): OrderDTO = CoroutineConfiguration.ioCoroutineScope.async {
//            orderRepository.findById(id)
//        }.await()
//    }

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