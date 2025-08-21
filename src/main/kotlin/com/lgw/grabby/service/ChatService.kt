package com.lgw.grabby.service

import com.lgw.grabby.common.AiModel
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.ai.anthropic.api.AnthropicApi
import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.stereotype.Service
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer

/**
 * OpenAI API를 사용하여 질의응답을 수행하는 서비스
 */
@Service
class ChatService(
    private val meterRegistry: MeterRegistry,
    private val openAiApi: OpenAiApi,
    private val anthropicApi: AnthropicApi,
    private val ollamaApi: OllamaApi
) {
    private val logger = KotlinLogging.logger {}
    val timerSample = Timer.start(meterRegistry)

    /**
     * OpenAI 챗 API를 이용하여 응답을 생성합니다.
     *
     * @param userInput 사용자 입력 메시지
     * @param systemMessage 시스템 프롬프트
     * @param model 사용할 LLM 모델명
     * @return 챗 응답 객체, 오류 시 null
     */
    fun openAiChat(
        userInput: String,
        systemMessage: String,
        model: String? = AiModel.GPT_3_5_TURBO
    ): ChatResponse? {
        logger.info { "OpenAI 챗 호출 시작 - 모델: $model" }
        val tags = listOf(model?.let { Tag.of("model", it) })
        try {
            // 메시지 구성
            val messages = listOf(
                SystemMessage(systemMessage),
                UserMessage(userInput)
            )

            // 챗 옵션 설정
            val chatOptions = ChatOptions.builder()
                .model(model)
                .build()

            // 프롬프트 생성
            val prompt = Prompt(messages, chatOptions)

            // 챗 모델 생성 및 호출
            val chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .build()

            val response = chatModel.call(prompt)

            // 타이머 완료(성공)
            timerSample.stop(Timer.builder("ai.chat.request")
                .description("OpenAI chat latency")
                .tags(tags + Tag.of("status", "success"))
                .register(meterRegistry))

            // 성공 카운터
            meterRegistry.counter("ai.chat.count", tags + Tag.of("status", "success")).increment()
            return response
        } catch (e: Exception) {
            // 타이머 완료(실패)
            timerSample.stop(Timer.builder("ai.chat.request")
                .description("OpenAI chat latency")
                .tags((tags + Tag.of("status", "error")).toString(),
                    Tag.of("error_type", e.javaClass.simpleName).toString()
                )
                .register(meterRegistry))

            // 실패 카운터
            meterRegistry.counter("ai.chat.count", (tags + Tag.of("status", "error")).toString(),
                Tag.of("error_type", e.javaClass.simpleName).toString()
            ).increment()
            logger.error(e) { "OpenAI 챗 호출 중 오류 발생: ${e.message}" }
            return null
        }
    }

    fun anthropicAiChat(
        userInput: String,
        systemMessage: String,
        model: String? = AiModel.CLAUDE_V3_SONNET
    ): ChatResponse? {
        logger.info { "Anthropic 챗 호출 시작 - 모델: $model" }
        try {
            // 메시지 구성
            val messages = listOf(
                SystemMessage(systemMessage),
                UserMessage(userInput)
            )

            // 챗 옵션 설정
            val chatOptions = ChatOptions.builder()
                .model(model)
                .build()

            // 프롬프트 생성
            val prompt = Prompt(messages, chatOptions)

            val chatModel = AnthropicChatModel.builder()
                .anthropicApi(anthropicApi)
                .build()

            // Anthropic API 호출
            return chatModel.call(prompt)
        } catch (e: Exception) {
            logger.error(e) { "Anthropic 챗 호출 중 오류 발생: ${e.message}" }
            return null
        }
    }

    fun ollamaAiChat(
        userInput: String,
        systemMessage: String,
        model: String? = AiModel.LLAMA3
    ): ChatResponse? {
        logger.info { "Ollama 챗 호출 시작 - 모델: $model" }
        return try {
            val messages = listOf(
                SystemMessage(systemMessage),
                UserMessage(userInput)
            )

            val chatOptions = ChatOptions.builder()
                .model(model)
                .temperature(0.2)
                .build()

            val prompt = Prompt(messages, chatOptions)

            val chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .build()

            chatModel.call(prompt)
        } catch (e: Exception) {
            logger.error(e) { "Ollama 챗 호출 중 오류 발생: ${e.message}" }
            null
        }
    }
}