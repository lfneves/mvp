package com.mvp.delivery.delivery.client

import com.mvp.delivery.delivery.model.User
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

@Service
class UserWebClient(webClient: WebClient) {
    var logger = LoggerFactory.getLogger(UserWebClient::class.java)
    private val webClient: WebClient

    init {
        this.webClient = webClient
    }

    fun retrieveGuestUser(userId: Int): Mono<User> {
        return webClient
            .get()
            .uri(USERS_URL_TEMPLATE, userId)
            .retrieve()
            .onStatus(
                { obj: HttpStatusCode -> obj.is5xxServerError() },
                {
                    Mono.error(
                        RuntimeException(
                            "There is an error while retrieving the user: $userId"
                        )
                    )
                })
            .bodyToMono(User::class.java)
            .doOnError { error -> logger.error("There is an error while sending request {}", error.message) }
            .onErrorResume { error -> Mono.just(User()) }
            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(3)))
            .map { user ->
                user.score = ThreadLocalRandom.current().nextInt(1, 100)
                user
            }
    }

    companion object {
        private const val USERS_URL_TEMPLATE = "/users/{id}"
    }
}