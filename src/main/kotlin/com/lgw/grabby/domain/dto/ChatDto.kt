package com.lgw.grabby.domain.dto

import com.lgw.grabby.common.AiModel
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 채팅 관련 DTO 클래스들
 * 추후 파이프라인이 늘어나게 된다면 ... 이곳에 추가할 예정
 */

/**
 * 채팅 요청 데이터 모델
 */
@Schema(description = "채팅 요청 데이터 모델")
data class ChatRequestDto(
    @Schema(description = "사용자 질문", example = "안녕하세요")
    val query: String,

    @Schema(description = "사용할 LLM 모델", example = AiModel.GPT_3_5_TURBO, defaultValue = AiModel.GPT_3_5_TURBO)
    val model: String? = AiModel.GPT_3_5_TURBO
)
