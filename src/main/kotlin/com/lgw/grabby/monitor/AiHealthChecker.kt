// kotlin
package com.lgw.grabby.monitor

import com.lgw.grabby.common.AiModel
import com.lgw.grabby.common.AiProvider
import com.lgw.grabby.domain.dto.HealthCheckResultDto
import com.lgw.grabby.service.ChatService
import org.springframework.beans.factory.ObjectProvider
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component

@Component
class AiHealthChecker(
    private val chatService: ChatService,
    private val environment: Environment
) {

    fun checkAll(): List<HealthCheckResultDto> = buildList {
        checkOpenAiWithChatService()?.let(::add)
        if (isLocal()) {
            checkOllamaWithChatService()?.let(::add)
        } else {
            checkAnthropicWithChatService()?.let(::add)
        }
    }

    private fun isLocal(): Boolean = environment.acceptsProfiles(Profiles.of("local"))
    private val query = "Hello, AI!"
    private val systemMessage = "You are a helpful AI assistant."

    /**
     *  TODO: 현재 ping 방식 사용이 안되어, 일반적인 chat의 응답값을 기준으로 통신이 정상적으로 된다면 health라고 판단
     *  추후 로직 리펙토링 필요. /ex ) ping 파이프라인을 따로 만들어서 사용
     */
    private fun checkOpenAiWithChatService(): HealthCheckResultDto? =
        chatService.openAiChat(query, systemMessage, AiModel.GPT_3_5_TURBO)
            ?.let { response ->
                HealthCheckResultDto(
                    provider = AiProvider.OPENAI.value,
                    status = response != null,
                    latencyMs = 0, // 필요시 시간 측정 추가
                    error = null
                )
            }

    private fun checkAnthropicWithChatService(): HealthCheckResultDto? =
        chatService.anthropicAiChat(query, systemMessage, AiModel.CLAUDE_V3_HAIKU)
            ?.let { response ->
                HealthCheckResultDto(
                    provider = AiProvider.ANTHROPIC.value,
                    status = response != null,
                    latencyMs = 0,
                    error = null
                )
            }

    private fun checkOllamaWithChatService(): HealthCheckResultDto? =
        chatService.ollamaAiChat(query, systemMessage, AiModel.LLAMA3)
            ?.let { response ->
                HealthCheckResultDto(
                    provider = AiProvider.OLLAMA.value,
                    status = response != null,
                    latencyMs = 0,
                    error = null
                )
            }

    // Kotlin extension for ObjectProvider null-safe access
    private fun <T> ObjectProvider<T>.ifAvailable(): T? = try {
        this.ifAvailable
    } catch (_: NoSuchMethodError) {
        // Fallback for older Spring versions
        this.getIfAvailable()
    }
}