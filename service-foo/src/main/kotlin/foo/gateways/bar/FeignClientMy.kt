package foo.gateways.bar

import feign.hystrix.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("service-bar", fallbackFactory = FeignClientFallbackFactory::class)
interface FeignClientMy {
    @GetMapping("/bar")
    fun helloString(): Map<String, Any>
}

object FeignClientMyFallback : FeignClientMy {
    override fun helloString(): Map<String, Any> {
        return mapOf("msg" to "Hello Fallback!")
    }
}

@Component
class FeignClientFallbackFactory : FallbackFactory<FeignClientMy> {
    override fun create(cause: Throwable): FeignClientMy {
        System.err.println("Fallback triggered because original request failed with an exception: $cause" )
        return FeignClientMyFallback
    }
}
