package com.lgw.grabby.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Ollama API 설정
 */
@Configuration
class OllamaConfig {
    private val logger = KotlinLogging.logger {}

    @Value("\${spring.ai.ollama.base-url:http://localhost:11434}")
    private lateinit var ollamaBaseUrl: String

    @Bean
    fun ollamaApi(): OllamaApi {
        logger.info { "Ollama API 클라이언트 초기화, Base URL: $ollamaBaseUrl" }
        return OllamaApi(ollamaBaseUrl)
    }
}