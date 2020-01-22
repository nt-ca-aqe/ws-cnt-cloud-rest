package foo.gateways.bar

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.netflix.loadbalancer.ILoadBalancer
import com.netflix.loadbalancer.Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit.jupiter.SpringExtension
import utils.FeignClientTestConfiguration
import utils.SimpleWireMockExtension

@ComponentScan
class WireMockExtensionBasedIntegrationTestConfiguration : FeignClientTestConfiguration()

@SpringBootTest(
        classes = [WireMockExtensionBasedIntegrationTestConfiguration::class], properties = ["eureka.client.enabled=false"]
)
@ExtendWith(SimpleWireMockExtension::class, SpringExtension::class)
internal class WireMockExtensionBasedIntegrationTest(@Autowired val feignClientMy: FeignClientMy) {

    @MockBean lateinit var loadBalancer: ILoadBalancer

    @BeforeEach fun directRibbonToWireMock(wireMock: WireMockServer) {
        given(loadBalancer.chooseServer(any())).willReturn(Server("localhost", wireMock.port()))
    }

    @Test
    fun `if no server is available, the fallback is invoked`() {
        val result = feignClientMy.helloString()
        assertThat(result["msg"]).isEqualTo("Hello Fallback!")
    }

    @Test
    fun `if a server is available, it will be used`(wireMock: WireMockServer) {
        wireMock.givenThat(get(urlEqualTo("/bar"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"msg": "Hello WireMock!"}""")))

        val result = feignClientMy.helloString()
        assertThat(result["msg"]).isEqualTo("Hello WireMock!")
    }

}
