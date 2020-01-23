import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}
repositories { mavenCentral(); mavenLocal(); jcenter() }

dependencies {
    val springBoot = "2.2.3.RELEASE"
//	implementation(kotlin("stdlib"))
//	implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:springBoot"))
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    implementation("org.springframework.cloud:spring-cloud-config-client")
}

tasks { withType<KotlinCompile> { kotlinOptions { jvmTarget = "11" } } }

dependencyManagement {
    imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:Finchley.RELEASE") }
}

