import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
	kotlin("kapt") version "1.8.21"
}

group = "com.mvp.delivery"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {

	//Spring
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
	implementation("org.springframework.session:spring-session-core:3.0.0")
	implementation("org.springframework.boot:spring-boot-docker-compose")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")
	implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
	testImplementation("io.projectreactor:reactor-test:3.5.4")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.1.0")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// CPF Validator
	implementation("br.com.colman.simplecpfvalidator:simple-cpf-validator:2.5.1")

	// Mapstruct
//	implementation("org.mapstruct:mapstruct:1.5.5.Final")
//	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	// Spring docker compose
	//developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
