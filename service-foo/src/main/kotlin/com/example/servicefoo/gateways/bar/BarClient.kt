package com.example.servicefoo.gateways.bar

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient("service-bar", fallbackFactory = BarClientFallbackFactory::class)
interface BarClient {

    @GetMapping("/bar")
    fun get(): Map<String, Any>

}