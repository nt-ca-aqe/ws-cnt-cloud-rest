plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

repositories { mavenCentral(); mavenLocal(); jcenter() }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine")
}

tasks {
    val java = "11"
    test { useJUnitPlatform() }
    compileKotlin { kotlinOptions { jvmTarget = java }; sourceCompatibility = java; targetCompatibility = java }
}
val cloud: String by project
dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$cloud") } }

