### Spring cloud - feign client testing with WireMock, Junit 5 & Spring Boot (Eureka server)

* **Eureka server** minimal dependencies in yor `build.gradle.kts` file:
```kotlin
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server")
}
dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$cloud") } }
```

* **Rest controller microservice** minimal dependencies in yor `build.gradle.kts` file:
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine")
}

```

### Feign client

```kotlin

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
```

### Test

```kotlin
@FeignClient("service-bar", fallbackFactory = FeignClientFallbackFactory::class)
interface FeignClientMy {
    @GetMapping("/bar")
    fun helloString(): Map<String, Any>
}

object FeignClientMyFallback : FeignClientMy {
    override fun helloString(): Map<String, Any> = mapOf("msg" to "Hello Fallback!")
}

@Component
class FeignClientFallbackFactory : FallbackFactory<FeignClientMy> {
    override fun create(cause: Throwable): FeignClientMy {
        System.err.println("Fallback triggered because original request failed with an exception: $cause" )
        return FeignClientMyFallback
    }
}

```
