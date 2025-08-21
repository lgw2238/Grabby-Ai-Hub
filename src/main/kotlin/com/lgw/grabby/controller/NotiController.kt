package com.lgw.grabby.controller

import com.lgw.grabby.domain.dto.ApiResponseDto
import com.lgw.grabby.notification.CompositeNotifier
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * Notification API 컨트롤러
 *
 * 알림(Email/KaKao) 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/v1/notification")
@Tag(name = "Noti API", description = "OpenAI API를 통한 채팅 기능")
class NotiController(
    private val notifier: CompositeNotifier
) {
    private val logger = KotlinLogging.logger {}

    /**
     * KaKao 알림 테스트 엔드포인트
     */
    @Operation(
        summary = "KaKao 알림 테스트",
        description = "KaKao 알림 기능을 테스트합니다. 이 엔드포인트는 카카오톡으로 알림을 전송합니다."
    )
    @SwaggerResponse(
        responseCode = "200",
        description = "KaKao Test Success",
        content = [Content(schema = Schema(implementation = ApiResponseDto::class))]
    )
    @SwaggerResponse(responseCode = "400", description = "KaKao 알림 전송 실패")
    @SwaggerResponse(responseCode = "500", description = "서버 오류")
    @GetMapping("/internal/test/kakao-notify")
    fun test(): String {
        logger.info { "NotiController : Testing KakaoNotifier" }
        notifier.notify("테스트 알림", "KakaoNotifier 전송 테스트입니다.")
        return "NotiController Test Done"
    }
}