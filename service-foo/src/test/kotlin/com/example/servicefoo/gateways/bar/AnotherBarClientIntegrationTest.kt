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
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace.*
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
import org.springframework.cloud.netflix.ribbon.StaticServerList
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
        classes = [AnotherBarClientIntegrationTest.TestConfiguration::class],
        properties = [
            "eureka.client.enabled=false",
            "feign.hystrix.enabled=true",
            "hystrix.command.default.execution.timeout.enabled=false",
            "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000",
            "hystrix.command.default.execution.isolation.semaphore.timeoutInMilliseconds=5000",
            "hystrix.command.default.execution.isolation.strategy=SEMAPHORE",
            "feign.client.config.default.connectTimeout=5000",
            "feign.client.config.default.readTimeout=5000",
            "feign.client.config.default.loggerLevel=FULL",
            "ribbon.ConnectTimeout=5000",
            "ribbon.ReadTimeout=5000",
            "ribbon.maxAutoRetries=1"
        ]
)
@ExtendWith(WireMockExtension::class, SpringExtension::class)
internal class AnotherBarClientIntegrationTest(
        @Autowired val cut: BarClient
) {

    @ImportAutoConfiguration(
            FeignAutoConfiguration::class,
            FeignRibbonClientAutoConfiguration::class,
            RibbonAutoConfiguration::class,
            HttpMessageConvertersAutoConfiguration::class,
            JacksonAutoConfiguration::class
    )
    @ComponentScan
    class TestConfiguration

    @MockBean lateinit var loadBalancer: ILoadBalancer

    @BeforeEach fun resetHystrix(): Unit = Hystrix.reset()
    @BeforeEach fun directRibbonToWireMock(wireMock: WireMockServer) {
        given(loadBalancer.chooseServer(any())).willReturn(Server("localhost", wireMock.port()))
    }

    @RepeatedTest(10)
    fun `if no server is available, the fallback is invoked`() {
        val result = cut.get()
        assertThat(result["msg"]).isEqualTo("Hello Fallback!")
    }

    @RepeatedTest(10)
    fun `if a server is available, it will be used`(wireMock: WireMockServer) {
        wireMock.givenThat(get(urlEqualTo("/bar"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"msg": "Hello WireMock!"}""")))

        val result = cut.get()
        assertThat(result["msg"]).isEqualTo("Hello WireMock!")
    }

}

class WireMockExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback, ParameterResolver {

    private companion object {
        const val WIRE_MOCK_SERVER = "WIRE_MOCK_SERVER"
    }

    override fun beforeAll(context: ExtensionContext) {
        val server = WireMockServer(0)
        server.start()
        context.getStore(GLOBAL).put(WIRE_MOCK_SERVER, server)
    }

    override fun afterAll(context: ExtensionContext) {
        context.getStore(GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java).stop()
    }

    override fun beforeEach(context: ExtensionContext) {
        context.getStore(GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java).resetMappings()
    }

    override fun supportsParameter(parameterContext: ParameterContext, context: ExtensionContext): Boolean {
        return parameterContext.parameter.type == WireMockServer::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, context: ExtensionContext): Any {
        return context.getStore(GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java)
    }

}
