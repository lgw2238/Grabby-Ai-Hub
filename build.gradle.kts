plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.lgw"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "1.0.0-M6"

// 전역적으로 commons-logging 제외
configurations.all {
	exclude(group = "commons-logging", module = "commons-logging")
}

val osxClassifier = when (System.getProperty("os.arch")) {
	"aarch64", "arm64" -> "osx-aarch_64"
	else -> "osx-x86_64"
}
// 프로젝트가 사용하는 Netty 버전에 맞게 설정 (예: 4.1.111.Final)
val nettyVersion = "4.1.119.Final"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.netty:netty-resolver-dns-native-macos:$nettyVersion:$osxClassifier")
	// Kotlin 지원
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Spring AI 의존성
	implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
	implementation("org.springframework.ai:spring-ai-anthropic-spring-boot-starter")
	implementation("org.springframework.ai:spring-ai-ollama")

	// PDF 처리 라이브러리
	implementation("org.apache.pdfbox:pdfbox:2.0.27")
	
	// Swagger/OpenAPI 의존성
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// 로깅 라이브러리
	implementation("io.github.oshai:kotlin-logging:6.0.3")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	// 선택: 분산 트레이싱(OTel 브리지 + OTLP 익스포터)
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")

	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-webflux") // Kakao API 호출(WebClient)

	// 테스트 의존성
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty(
		"spring.profiles.active",
		// 우선순위: -Dspring.profiles.active > SPRING_PROFILES_ACTIVE > 기본 local
		(project.findProperty("spring.profiles.active") as String?)
			?: System.getenv("SPRING_PROFILES_ACTIVE")
			?: "local"
	)
	environment("OPENAI_API_KEY", System.getenv("OPENAI_API_KEY") ?: "real-api-key")
	environment("ANTHROPIC_API_KEY", System.getenv("ANTHROPIC_API_KEY") ?: "real-api-key")
	environment("KAKAO_ACCESS_TOKEN", System.getenv("KAKAO_ACCESS_TOKEN") ?: "real-api-key")
}

tasks.test {
	systemProperty(
		"spring.profiles.active",
		System.getenv("SPRING_PROFILES_ACTIVE") ?: "test"
	)
}