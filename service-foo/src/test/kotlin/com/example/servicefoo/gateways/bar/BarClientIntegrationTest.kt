package com.example.servicefoo.gateways.bar

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.netflix.hystrix.Hystrix
import com.netflix.loadbalancer.ILoadBalancer
import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.ServerList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
import org.springframework.cloud.netflix.ribbon.StaticServerList
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
        classes = [BarClientIntegrationTest.TestConfiguration::class],
        properties = [
            "eureka.client.enabled=false",
            "feign.hystrix.enabled=true"
        ]
)
@ExtendWith(SpringExtension::class)
internal class BarClientIntegrationTest(
        @Autowired val cut: BarClient,
        @Autowired val wireMock: WireMockServer
) {

    @ImportAutoConfiguration(
            FeignAutoConfiguration::class,
            FeignRibbonClientAutoConfiguration::class,
            RibbonAutoConfiguration::class,
            HttpMessageConvertersAutoConfiguration::class,
            JacksonAutoConfiguration::class
    )
    @ComponentScan
    class TestConfiguration {

        @Bean fun wireMock(): WireMockServer = WireMockBean()

        @Bean fun ribbonServerList(server: WireMockServer): ServerList<Server> {
            return StaticServerList<Server>(Server("localhost", server.port()))
        }

        fun loadBalancer(server: WireMockServer): ILoadBalancer {
            return mock(ILoadBalancer::class.java).also {
                given(it.chooseServer(any())).willReturn(Server("localhost", server.port()))
            }
        }

    }

    @BeforeEach fun resetWireMock() {
        Hystrix.reset()
        wireMock.resetMappings()
    }

    @RepeatedTest(10)
    fun `if no server is available, the fallback is invoked`() {
        val result = cut.get()
        assertThat(result["msg"]).isEqualTo("Hello Fallback!")
    }

    @RepeatedTest(10)
    fun `if a server is available, it will be used`() {
        wireMock.givenThat(get(urlEqualTo("/bar"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"msg": "Hello WireMock!"}""")))

        val result = cut.get()
        assertThat(result["msg"]).isEqualTo("Hello WireMock!")
    }

}

