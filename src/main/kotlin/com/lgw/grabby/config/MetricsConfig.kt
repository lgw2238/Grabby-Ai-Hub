package com.lgw.grabby.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.micrometer.core.instrument.config.MeterFilter

@Configuration
class MetricsConfig(
    @Value("\${spring.application.name:grabby-ai}")
    private val appName: String,
    @Value("\${management.metrics.tags.env:local}")
    private val env: String
) {
    @Bean
    fun meterRegistryCustomizer(): (MeterRegistry) -> Unit = { registry ->
        registry.config()
            .commonTags("application", appName, "env", env)
            .meterFilter(MeterFilter.denyNameStartsWith("jvm.threads.states")) // 예시: 과다 메트릭 억제
    }
}