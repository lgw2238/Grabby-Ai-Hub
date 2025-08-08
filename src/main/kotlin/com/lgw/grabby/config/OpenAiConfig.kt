package com.lgw.grabby.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAI API 설정
 */
@Configuration
class OpenAiConfig {
    private val logger = KotlinLogging.logger {}

    @Value("\${spring.ai.openai.api-key}")
    private lateinit var apiKey: String

    /**
     * OpenAI API 클라이언트 빈 등록
     */
    @Bean
    fun openAiApi(): OpenAiApi {
        logger.info { "OpenAI API 클라이언트 초기화" }
        return OpenAiApi.builder()
            .apiKey(apiKey)
            .build()
    }
}