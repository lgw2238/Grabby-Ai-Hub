package com.lgw.grabby.controller

import com.lgw.grabby.domain.dto.ApiResponseDto
import com.lgw.grabby.domain.dto.ChatRequestDto
import com.lgw.grabby.service.ChatService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Chat API 컨트롤러
 *
 * LLM API를 통해 채팅 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat API", description = "OpenAI API를 통한 채팅 기능")
class ChatController(
    private val chatService: ChatService
) {
    private val logger = KotlinLogging.logger {}

    /**
     * 사용자의 메시지를 받아 OpenAI API로 응답 생성
     */
    @Operation(
        summary = "LLM 채팅 메시지 전송",
        description = "사용자의 메시지를 받아 OpenAI API를 통해 응답을 생성합니다."
    )
    @SwaggerResponse(
        responseCode = "200",
        description = "LLM 응답 성공",
        content = [Content(schema = Schema(implementation = ApiResponseDto::class))]
    )
    @SwaggerResponse(responseCode = "400", description = "잘못된 요청")
    @SwaggerResponse(responseCode = "500", description = "서버 오류")
    @PostMapping("/query")
    fun sendMessage(
        @Parameter(description = "채팅 요청 객체", required = true)
        @RequestBody request: ChatRequestDto
    ): ResponseEntity<ApiResponseDto<Map<String, Any>>> {
        logger.info { "Chat API 요청 받음: model=${request.model}" }
        // 유효성 검사
        if (request.query.isBlank()) {
            logger.warn { "빈 질의가 요청됨" }
            return ResponseEntity.badRequest().body(
                ApiResponseDto(success = false, error = "질의가 비어있습니다.")
            )
        }

        return try {
            // 시스템 프롬프트 지정
            val systemMessage = "You are a helpful AI assistant."

            /**
             *  모델별 인스턴스가 달라, 비즈니스 레이어에서의 모델별 분기가 되어 있어
             *  Controller에서도 해당 모델을 체크하여 로직을 분기한다.
             *  TODO : 추후 해당 로직을 조금 더 간결하고, side effect가 없도록 개선할 필요가 있다.
             */
            val response = request.model?.let {
                if (it.contains("claude", ignoreCase = true)) {
                    chatService.anthropicAiChat(
                        userInput = request.query,
                        systemMessage = systemMessage,
                        model = it
                    )
                } else {
                    chatService.openAiChat(
                        userInput = request.query,
                        systemMessage = systemMessage,
                        model = it
                    )
                }
            }
            logger.debug { "LLM 응답 생성: $response" }

            response?.let { chatResponse ->
                ResponseEntity.ok(
                    ApiResponseDto(
                        success = true,
                        data = mapOf("answer" to chatResponse.result.output.text)
                    )
                )
            } ?: run {
                logger.error { "LLM 응답 생성 실패" }
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseDto(
                        success = false,
                        error = "LLM 응답 생성 중 오류 발생"
                    )
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Chat API 처리 중 오류 발생" }
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseDto(
                    success = false,
                    error = e.message ?: "알 수 없는 오류 발생"
                )
            )
        }
    }
}
