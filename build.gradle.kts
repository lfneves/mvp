import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
//	kotlin("kapt") version "1.8.21"
}

group = "com.mvp.delivery"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {

	//Spring
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-security:3.1.1")
	implementation("org.springframework.session:spring-session-core:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-cache:3.1.1")
	implementation("org.springframework.boot:spring-boot-docker-compose:3.1.1")

	// Kotlin utils
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

	// Postgress reactive
	implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")

	// Coroutines and Reactor not used yet! (used to more imperative reactive programing with suspend functions)
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")

	// Mercado Pago SDK
	//implementation("com.mercadopago:sdk-java:2.1.11")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.projectreactor:reactor-test:3.5.4")
	testImplementation("io.mockk:mockk:1.13.4")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.1.0")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// CPF Validator
	//implementation("br.com.colman.simplecpfvalidator:simple-cpf-validator:2.5.1")

	// Mapstruct (applies when application growing - need kapt)
	//implementation("org.mapstruct:mapstruct:1.5.5.Final")
	//annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	enabled = false
	useJUnitPlatform()
}
