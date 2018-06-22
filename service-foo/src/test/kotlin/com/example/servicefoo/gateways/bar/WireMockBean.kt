package com.example.servicefoo.gateways.bar

import com.github.tomakehurst.wiremock.WireMockServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

class WireMockBean : WireMockServer(0) {

    @PostConstruct fun postConstruct() {
        start()
    }

    @PreDestroy fun preDestroy() {
        stop()
    }

}