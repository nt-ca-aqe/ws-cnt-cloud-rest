package com.example.servicebar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@EnableDiscoveryClient
@SpringBootApplication
class ServiceBarApplication

fun main(args: Array<String>) {
    runApplication<ServiceBarApplication>(*args)
}


@RestController
@RequestMapping("/bar")
class BatController {

    @GetMapping
    fun get(): Map<String, Any> {
        return mapOf("msg" to "Hello Bar!")
    }

}