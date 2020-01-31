package foo.api

import foo.gateways.bar.FeignClientMy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/foo")
class FooController(
        private val feignClientMy: FeignClientMy
) {

    @GetMapping
    fun get(): Map<String, Any?> {
        val barData = feignClientMy.helloString()
        return mapOf(
                "msg" to barData["msg"],
                "answer" to 42
        )
    }

}
