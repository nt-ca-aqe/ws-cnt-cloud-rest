package com.tsconsulting.biometrics.info.config

import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.jupiter.api.extension.*


class WireMockExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback, ParameterResolver {
    private companion object {
        const val WIRE_MOCK_SERVER = "WIRE_MOCK_SERVER"
    }
    override fun beforeAll(context: ExtensionContext): Unit = with(WireMockServer(0)) {
        start(); context.getStore(ExtensionContext.Namespace.GLOBAL).put(WIRE_MOCK_SERVER, this)
    }
    override fun afterAll(context: ExtensionContext): Unit =
        context.getStore(ExtensionContext.Namespace.GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java).stop()

    override fun beforeEach(context: ExtensionContext): Unit =
        context.getStore(ExtensionContext.Namespace.GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java).resetMappings()
    override fun supportsParameter(parameterContext: ParameterContext, context: ExtensionContext): Boolean =
        parameterContext.parameter.type == WireMockServer::class.java
    override fun resolveParameter(parameterContext: ParameterContext, context: ExtensionContext): Any =
        context.getStore(ExtensionContext.Namespace.GLOBAL).get(WIRE_MOCK_SERVER, WireMockServer::class.java)
}
