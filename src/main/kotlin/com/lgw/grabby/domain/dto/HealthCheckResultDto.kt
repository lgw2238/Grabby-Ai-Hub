package com.lgw.grabby.domain.dto

data class HealthCheckResultDto(
    val provider: String,
    val status: Boolean,
    val latencyMs: Long,
    val error: String? = null
)
