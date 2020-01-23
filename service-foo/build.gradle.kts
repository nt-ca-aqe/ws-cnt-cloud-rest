plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("com.github.ben-manes.versions") version "0.27.0"
    id("org.springframework.boot") version "2.2.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

repositories { mavenCentral(); mavenLocal(); jcenter() }

dependencies {
    // Web
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
//    { exclude("org.junit.vintage", "junit-vintage-engine") }
    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine")
    testImplementation("org.springframework.cloud", "spring-cloud-contract-wiremock")
    // Cloud
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-hystrix")
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-ribbon")
    implementation("org.springframework.cloud", "spring-cloud-starter-openfeign")

    // runtimeOnly("org.springframework.boot:spring-boot-devtools")
    // annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks {
    val java = "11"
    test { useJUnitPlatform() }
    compileKotlin { kotlinOptions { jvmTarget = java }; sourceCompatibility = java; targetCompatibility = java }
}
val cloud: String by project

dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$cloud") } }

