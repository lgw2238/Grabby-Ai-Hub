package com.lgw.grabby.common

object AiModel {
    const val GPT_3_5_TURBO = "gpt-3.5-turbo"
    const val GPT_4 = "gpt-4"
    const val GPT_4_TURBO = "gpt-4-turbo"
    const val LLAMA3 = "llama3"
    const val LLM_MATH = "llm-math"
    const val LLM_CODE_INTERPRETER = "llm-code-interpreter"
    const val LLM_DALL_E = "llm-dall-e"
    const val CLAUDE_V1 = "claude-v1"

    private val models = listOf(
        GPT_3_5_TURBO,
        GPT_4,
        GPT_4_TURBO,
        LLAMA3,
        LLM_MATH,
        LLM_CODE_INTERPRETER,
        LLM_DALL_E,
        CLAUDE_V1
    )

    fun fromString(model: String): String? {
        return models.find { it.equals(model, ignoreCase = true) }
    }
}