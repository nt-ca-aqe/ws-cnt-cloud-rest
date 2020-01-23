import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}
repositories { mavenCentral(); mavenLocal(); jcenter() }

val springBoot = "2.2.3.RELEASE"
val cloud = "Hoxton.SR1"

dependencies {
    //    implementation("org.springframework.cloud:spring-cloud-config-client")
    implementation(kotlin("stdlib"))
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server")
}

dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$cloud") } }

tasks { withType<KotlinCompile> { kotlinOptions { jvmTarget = "11" } } }

