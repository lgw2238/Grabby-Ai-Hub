package com.lgw.grabby.common

object AiModel {
    const val GPT_3_5_TURBO = "gpt-3.5-turbo"
    const val GPT_4 = "gpt-4"
    const val GPT_4_TURBO = "gpt-4-turbo"
    const val LLAMA3 = "llama3"
    const val LLM_MATH = "llm-math"
    const val LLM_CODE_INTERPRETER = "llm-code-interpreter"
    const val LLM_DALL_E = "llm-dall-e"
    const val CLAUDE_V3_HAIKU = "claude-3-haiku-20240307"
    const val CLAUDE_V3_SONNET = "claude-3-sonnet-20240229"
    const val CLAUDE_V3_OPUS = "claude-3-opus-20240229"

    private val gptModels = listOf(
        GPT_3_5_TURBO,
        GPT_4,
        GPT_4_TURBO,
        LLAMA3,
        LLM_MATH,
        LLM_CODE_INTERPRETER,
        LLM_DALL_E,
    )

    private val anthropicModels = listOf(
        CLAUDE_V3_HAIKU,
        CLAUDE_V3_SONNET,
        CLAUDE_V3_OPUS
    )

}