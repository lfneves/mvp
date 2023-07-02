package com.mvp.delivery.domain.configuration

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

//Not used yet
object CoroutineConfiguration {

    private val coroutineDispatcher = Executors.newFixedThreadPool(100).asCoroutineDispatcher()
    /**
     * SupervisorJob pois cada rotina solicitada para executar pode falhar independente de outras. Caso uma falhe, outras
     * poderão continuar sendo executadas sem problemas, detalhe que ele só lida com a excessão CancellationException,
     * outras excessões ele continua não sabendo lidar e trava tudo, por isso usar o try catch.
     * Para mais info:
     *
     * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-supervisor-job.html
     */
    val scope = CoroutineScope(coroutineDispatcher + CoroutineName("database") + SupervisorJob())
}