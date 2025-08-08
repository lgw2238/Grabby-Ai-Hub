package com.lgw.grabby.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * API 표준 응답 포맷
 *
 * 모든 API 응답의 Common Formatting
 */
@Schema(description = "API 표준 응답 포맷")
data class ApiResponseDto<T>(
    @Schema(description = "요청 처리 성공 여부")
    val success: Boolean,

    @Schema(description = "응답 데이터 (성공 시)")
    val data: T? = null,

    @Schema(description = "오류 메시지 (실패 시)")
    val error: String? = null
)
