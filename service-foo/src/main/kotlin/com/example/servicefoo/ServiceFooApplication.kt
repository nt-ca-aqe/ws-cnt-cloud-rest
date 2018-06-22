package com.example.servicefoo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
class ServiceFooApplication

fun main(args: Array<String>) {
    runApplication<ServiceFooApplication>(*args)
}


@RestController
@RequestMapping("/foo")
class FooController(
        private val barClient: BarClient
) {

    @GetMapping
    fun get(): Map<String, Any> {
        return barClient.get()
    }

}

@FeignClient("service-bar", fallback = BarClientFallback::class)
interface BarClient {

    @GetMapping("/bar")
    fun get(): Map<String, Any>

}

@Component
class BarClientFallback : BarClient {

    override fun get(): Map<String, Any> {
        return mapOf("msg" to "Hello World!")
    }

}