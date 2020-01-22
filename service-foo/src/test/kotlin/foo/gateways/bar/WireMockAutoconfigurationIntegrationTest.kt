package foo.gateways.bar

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.ServerList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.cloud.netflix.ribbon.StaticServerList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit.jupiter.SpringExtension
import utils.FeignClientTestConfiguration

@ComponentScan
class WireMockAutoconfigurationIntegrationTestConfiguration : FeignClientTestConfiguration() {
    @Bean fun ribbonServerList(@Value("\${wiremock.server.port}") wireMockPort: Int): ServerList<Server> {
        return StaticServerList<Server>(Server("localhost", wireMockPort))
    }
}

@SpringBootTest(
        classes = [WireMockAutoconfigurationIntegrationTestConfiguration::class], properties = ["eureka.client.enabled=false"]
)
@AutoConfigureWireMock(port = 0)
@ExtendWith(SpringExtension::class)
internal class WireMockAutoconfigurationIntegrationTest(
    @Autowired val cut: FeignClientMy, @Autowired val wireMock: WireMockServer
) {

    @BeforeEach fun resetWireMock(): Unit = wireMock.resetMappings()

    @Test
    fun `if no server is available, the fallback is invoked`() {
        val result = cut.helloString()
        assertThat(result["msg"]).isEqualTo("Hello Fallback!")
    }

    @Test
    fun `if a server is available, it will be used`() {
        wireMock.givenThat(get(urlEqualTo("/bar"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"msg": "Hello WireMock!"}""")))

        val result = cut.helloString()
        assertThat(result["msg"]).isEqualTo("Hello WireMock!")
    }

}
