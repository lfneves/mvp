package com.mvp.delivery.domain.model.order.store.webhook


import com.fasterxml.jackson.annotation.JsonProperty

data class Collector(
    @JsonProperty("email")
    var email: String?,
    @JsonProperty("id")
    var id: Int?,
    @JsonProperty("nickname")
    var nickname: String?
)