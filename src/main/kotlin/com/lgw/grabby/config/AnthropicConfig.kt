package com.lgw.grabby.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.anthropic.api.AnthropicApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Anthropic API 설정
 */
@Configuration
class AnthropicConfig {
    private val logger = KotlinLogging.logger {}

    @Value("\${spring.ai.anthropic.api-key}")
    private lateinit var apiKey: String

    /**
     * AnthropicApi 클라이언트 빈 등록
     */
    @Bean
    fun anthropicApi(): AnthropicApi {
        logger.info { "Anthropic API 클라이언트 초기화"}
        return AnthropicApi(apiKey)

    }
}