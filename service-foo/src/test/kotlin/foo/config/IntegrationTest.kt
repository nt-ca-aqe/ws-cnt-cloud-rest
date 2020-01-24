package com.tsconsulting.biometrics.info.config

import com.tsconsulting.biometrics.info.service.WireMockExtensionBasedIntegrationTestConfiguration
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntegrationTest


@SpringBootTest(classes = [WireMockExtensionBasedIntegrationTestConfiguration::class], properties = ["eureka.client.enabled=false"])
@ExtendWith(WireMockExtension::class, SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WireMockIntegrationTest


@ImportAutoConfiguration(
    FeignAutoConfiguration::class,
    FeignRibbonClientAutoConfiguration::class,
    RibbonAutoConfiguration::class,
    HttpMessageConvertersAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
open class FeignClientTestConfiguration
