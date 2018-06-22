package com.example.servicefoo.api

import com.example.servicefoo.gateways.bar.BarClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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