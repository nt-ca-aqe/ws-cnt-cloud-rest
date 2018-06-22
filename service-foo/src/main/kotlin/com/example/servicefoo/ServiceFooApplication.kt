package com.example.servicefoo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableDiscoveryClient
@SpringBootApplication
class ServiceFooApplication

fun main(args: Array<String>) {
    runApplication<ServiceFooApplication>(*args)
}


