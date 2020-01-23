import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}
repositories { mavenCentral(); mavenLocal(); jcenter() }

val cloud: String by project
val springBoot: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server")
    // implementation("org.springframework.cloud", "spring-cloud-config-client")
}

dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$cloud") } }

tasks { withType<KotlinCompile> { kotlinOptions { jvmTarget = "11" } } }

