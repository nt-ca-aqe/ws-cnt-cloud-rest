package com.example.servicefoo.gateways.bar

import feign.hystrix.FallbackFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

class BarClientFallback : BarClient {

    override fun get(): Map<String, Any> {
        return mapOf("msg" to "Hello Fallback!")
    }

}

@Component
class BarClientFallbackFactory : FallbackFactory<BarClient> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun create(cause: Throwable): BarClient {
        log.error("failed because of an exception: ", cause)
        return BarClientFallback()
    }

}