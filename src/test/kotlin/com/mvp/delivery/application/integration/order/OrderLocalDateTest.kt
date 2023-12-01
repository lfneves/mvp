package com.mvp.delivery.application.integration.order

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class OrderLocalDateTest {
    @Test
    fun `Validate localDate plus minutes`() {
        val randomMinutes = (20..75).random().toLong()
        println(randomMinutes)
        val result = LocalDateTime.now().plusMinutes(randomMinutes).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()
        println(result)
    }
}